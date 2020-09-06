package com.example.lababsencesystem;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAddDeleteFragment extends Fragment {

    Button addManualButton,addManualCancelButton,addManualSubmitButton;
    LinearLayout linearLayoutAddManualAdmin;
    EditText FileNumberAddEditText,nameAddEditText,emailAddEditText;
    Spinner courseSpinnerAdmin;

    EditText enterIdAdmin,NameSearch,typeSearch;
    Button searchAdmin,cancelSearch,submitSearch;
    LinearLayout linearLayoutSearchAdmin;
    TextView erorrAdmin;
    ProgressBar prbarAdmin;

    private String fileNumber="";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<String> coursesCodeAdmin = new ArrayList<>();

    public AdminAddDeleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_add_delete, container, false);

        linearLayoutAddManualAdmin=view.findViewById(R.id.linearLayoutAddManualAdmin);
        addManualButton=view.findViewById(R.id.addManualButton);
        addManualCancelButton=view.findViewById(R.id.addManualCancelButton);
        addManualSubmitButton=view.findViewById(R.id.addManualSubmitButton);
        courseSpinnerAdmin=view.findViewById(R.id.courseSpinnerAdmin);
        FileNumberAddEditText=view.findViewById(R.id.FileNumberAdd);
        emailAddEditText=view.findViewById(R.id.emailAdd);
        nameAddEditText=view.findViewById(R.id.nameAdd);

        typeSearch=view.findViewById(R.id.typeSearch);
        NameSearch=view.findViewById(R.id.NameSearch);
        enterIdAdmin=view.findViewById(R.id.enterIdAdmin);
        searchAdmin=view.findViewById(R.id.searchAdmin);
        cancelSearch=view.findViewById(R.id.cancelSearch);
        submitSearch=view.findViewById(R.id.submitSearch);
        linearLayoutSearchAdmin=view.findViewById(R.id.linearLayoutSearchAdmin);
        erorrAdmin=view.findViewById(R.id.erorrAdmin);
        prbarAdmin=view.findViewById(R.id.prbarAdmin);

        linearLayoutAddManualAdmin.setVisibility(View.GONE);
        linearLayoutSearchAdmin.setVisibility(View.GONE);

        coursesCodeAdmin.clear();
        coursesCodeAdmin.add("student");
        coursesCodeAdmin.add("doctor");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style,coursesCodeAdmin);
        courseSpinnerAdmin.setAdapter(adapter);

        addManualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddManualAdmin.setVisibility(View.VISIBLE);
                nameAddEditText.setText("");
                emailAddEditText.setText("");
                FileNumberAddEditText.setText("");
            }
        });

        addManualCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddManualAdmin.setVisibility(View.GONE);
            }
        });

        addManualSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=nameAddEditText.getText().toString();
                String fileNb=FileNumberAddEditText.getText().toString();
                String email=emailAddEditText.getText().toString();
                String text = courseSpinnerAdmin.getSelectedItem().toString();

                int flag = 0;
                if (name.equals("")) {
                    nameAddEditText.setError("Name is Required");
                    flag=1;
                }
                if (fileNb.equals("")){
                    FileNumberAddEditText.setError("FileNumber is Required");
                    flag=1;
                }
                if(!isValidEmail(email)){
                    emailAddEditText.setError("Invalid Email Format");
                    flag=1;
                }
                if (email.equals("")){
                    emailAddEditText.setError("Email is Required");
                    flag=1;
                }
                if (flag==1)
                    return;

          //      Log.d("asssa",name+" ,"+email+" ,"+fileNb+" , "+text);
                if (text.equals("student")){
                    Log.d("asssa",name+" ,"+email+" ,"+fileNb+" , "+text);
                    Student st = new Student(name,email,fileNb,"666666",Integer.parseInt(fileNb),text);
                    db.collection("users").document("students").collection("data").document(fileNb).set(st);
                }
                else {
          //          Log.d("asssa",name+" ,"+email+" ,"+fileNb+" , "+text);
                    Doctor st = new Doctor(name,email,fileNb,"666666",Integer.parseInt(fileNb),text);
                    db.collection("users").document("doctors").collection("data").document(fileNb).set(st);
                }
                Toast.makeText(getActivity(),"Added",Toast.LENGTH_SHORT).show();
                linearLayoutAddManualAdmin.setVisibility(View.GONE);

            }
        });

        searchAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSearchAdmin.setVisibility(View.GONE);
                final String idToSearch = enterIdAdmin.getText().toString();
                if (!idToSearch.equals("")){
                    prbarAdmin.setVisibility(View.VISIBLE);
                    erorrAdmin.setVisibility(View.GONE);
                    searchAdmin.setEnabled(false);
                    db.collection("users").document("students").collection("data").document(idToSearch).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()){
                                    String name = task.getResult().getString("name");
                                    String type = task.getResult().getString("type");
                                    fileNumber=idToSearch;
                                    linearLayoutSearchAdmin.setVisibility(View.VISIBLE);
                                    NameSearch.setText(name);
                                    typeSearch.setText(type);
                                    searchAdmin.setEnabled(true);
                                    prbarAdmin.setVisibility(View.GONE);
                                }
                                else{
                                    db.collection("users").document("doctors").collection("data").document(idToSearch).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().exists()){
                                                    String name = task.getResult().getString("name");
                                                    String type = task.getResult().getString("type");
                                                    fileNumber=idToSearch;
                                                    linearLayoutSearchAdmin.setVisibility(View.VISIBLE);
                                                    NameSearch.setText(name);
                                                    typeSearch.setText(type);
                                                    searchAdmin.setEnabled(true);
                                                    prbarAdmin.setVisibility(View.GONE);
                                                }
                                                else{
                                                    linearLayoutSearchAdmin.setVisibility(View.GONE);
                                                    erorrAdmin.setText("FileNumber doesn't Exist");
                                                    erorrAdmin.setVisibility(View.VISIBLE);
                                                    searchAdmin.setEnabled(true);
                                                    prbarAdmin.setVisibility(View.GONE);

                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSearchAdmin.setVisibility(View.GONE);
                fileNumber="";
            }
        });

        submitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(typeSearch.getText().toString()+"s").collection("data").document(fileNumber).delete();
                linearLayoutSearchAdmin.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT).show();
            }
        });

        return  view;
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
