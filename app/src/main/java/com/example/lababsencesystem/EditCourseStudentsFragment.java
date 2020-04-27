package com.example.lababsencesystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import static android.app.Activity.RESULT_OK;

public class EditCourseStudentsFragment extends Fragment {


    ProgressBar progressBar;
    TextView textImport,textShow,eror;
    EditText enterStudentId;
    Button chooseFile,search,submit;
    Intent myFileIntent;
    List<String> name;
    List<String> id;
    Workbook workbook;
    ArrayList<Student> students;
    ArrayList<CourseStudent> cs;
    int flag=0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public EditCourseStudentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_edit_student, container, false);

        textShow=view.findViewById(R.id.textShow);
        textImport=view.findViewById(R.id.textImport);
        chooseFile=view.findViewById(R.id.chooseFile);
        enterStudentId=view.findViewById(R.id.enterStudentId);
        search=view.findViewById(R.id.search);
        submit=view.findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        progressBar=view.findViewById(R.id.prbar);
        progressBar.setVisibility(View.GONE);
        eror=view.findViewById(R.id.eror);
        eror.setVisibility(View.GONE);

        name = new ArrayList<>();
        id = new ArrayList<>();

        students=new ArrayList<>();
        cs=new ArrayList<>();

        String getCourseCode="";
        Intent intent=getActivity().getIntent();
        getCourseCode=intent.getStringExtra("CourseCode");
        textShow.setText(getCourseCode);


        final RecyclerView rv = view.findViewById(R.id.addDeleteRv);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        final String finalGetCourseCode = getCourseCode;

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                students.clear();
                cs.clear();
                eror.setVisibility(View.GONE);
                final String s=enterStudentId.getText().toString();
                if (!s.equals("")) {
                    search.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    final String[] strings = s.split("-");

                    if (strings.length == 2) {
                        flag=2;
                        db.collection("courses").document(finalGetCourseCode).collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){

                                    if (task.getResult().size() > 0)
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            if (document.getLong("fileNumber").intValue() >= Integer.parseInt(strings[0])
                                                    && document.getLong("fileNumber").intValue() <= Integer.parseInt(strings[1])){
                                                String name = document.getString("name");

                                                int filenb = document.getLong("fileNumber").intValue();
                                                CourseStudent ss = new CourseStudent(name, filenb);
                                                cs.add(ss);
                                            }

                                        }

                                    db.collection("users").document("students").collection("data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                int ln=cs.size();
                                                if (task.getResult().size() > 0)
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if (document.getLong("fileNumber").intValue() >= Integer.parseInt(strings[0])
                                                                && document.getLong("fileNumber").intValue() <= Integer.parseInt(strings[1])) {
                                                            String name = document.getString("name");
                                                            String username = document.getString("username");
                                                            String password = document.getString("password");
                                                            String email = document.getString("email");
                                                            String type = document.getString("type");
                                                            int filenb = document.getLong("fileNumber").intValue();

                                                            Student st = new Student(name, email, username, password, filenb, type);
                                                            students.add(st);


                                                        }
                                                    }
                                            }
                                            try {
                                                Thread.sleep(1000);
                                                for (int l=0;l<students.size();l++){
                                                    for (int o=0;o<cs.size();o++){
                                                        if (cs.get(o).getFileNumber() == students.get(l).getFileNumber())
                                                            students.remove(l);
                                                    }
                                                }
                                                search.setEnabled(true);
                                                RecyclerView.Adapter a = new DoctorAddDeleteStudentAdapter(students, cs, flag,finalGetCourseCode);
                                                rv.setAdapter(a);
                                                progressBar.setVisibility(View.GONE);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }

                            }
                        });

                    } else{

                        db.collection("courses").document(finalGetCourseCode).collection("students").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        String name = task.getResult().getString("name");
                                        int filenb = task.getResult().getLong("fileNumber").intValue();

                                        CourseStudent ss = new CourseStudent(name, filenb);
                                        cs.add(ss);
                                        flag = 1;

                                    } else {
                                        db.collection("users").document("students").collection("data").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        String name = task.getResult().getString("name");
                                                        String username = task.getResult().getString("username");
                                                        String password = task.getResult().getString("password");
                                                        String email = task.getResult().getString("email");
                                                        String type = task.getResult().getString("type");
                                                        int filenb = task.getResult().getLong("fileNumber").intValue();

                                                        Student st = new Student(name, email, username, password, filenb, type);
                                                        students.add(st);
                                                        flag = 0;
                                                    }
                                                    else{
                                                        eror.setText("File Number doesn't exists");
                                                        eror.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            }
                                        });

                                    }

                                    try {
                                        Thread.sleep(1000);
                                        search.setEnabled(true);
                                        RecyclerView.Adapter a = new DoctorAddDeleteStudentAdapter(students, cs, flag,finalGetCourseCode);
                                        rv.setAdapter(a);
                                        progressBar.setVisibility(View.GONE);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                }
                }

            }
        });

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                startActivityForResult(myFileIntent, 10);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int flag=0;
        String path="";
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    path = data.getData().getPath();
                    Log.d("tag3", path);
                    textImport.setText(path);
                    WorkbookSettings ws = new WorkbookSettings();
                    ws.setGCDisabled(true);
                    File file = new File(path);
                    if (file.exists()) {
                        try {
                            workbook = Workbook.getWorkbook(file);
                            Sheet sheet = workbook.getSheet(0);
                            int j;
                            for (j=0;j<sheet.getColumns();j++);
                            if (j!=2)
                                flag=1;
                            if (flag==0) {
                                for (int i = 0; i < sheet.getRows(); i++) {
                                    Cell[] row = sheet.getRow(i);
                                    id.add(row[0].getContents());
                                }
                                Log.d("tag4", id + ":");
                                Log.d("tag4", name + "");
                                submit.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(getActivity(),"invalid format",Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (BiffException e) {
                            Toast.makeText(getActivity(),"make sure your file .xls",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(),"File does not exist",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


}
