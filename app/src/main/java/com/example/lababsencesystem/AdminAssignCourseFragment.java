package com.example.lababsencesystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAssignCourseFragment extends Fragment {

    Button assignCourseSubmit;
    Spinner spinnerForDoctor,spinnerForCourse;
    LinearLayout assignLayout;

    EditText addCourseCodeEditText,addCourseCreditsEditText,addCourseNameEditText,addCourseDoctorEditText;
    TextView addCourseErrorTextView;
    Button addCourseCodeSubmit;

    Button deleteCourseCodeSubmit;
    Spinner spinnerForCourseDelete;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<String> courses = new ArrayList<>();
    ArrayList<String> doctors = new ArrayList<>();
    ArrayList<String> c = new ArrayList<>();

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterCourse;
    ArrayAdapter<String> adapterDelete;

    String nameD="";
    int idDr=0;

    public AdminAssignCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_assign_course, container, false);

        assignCourseSubmit=view.findViewById(R.id.assignCourseSubmit);
        spinnerForCourse=view.findViewById(R.id.spinnerForCourse);
        spinnerForDoctor=view.findViewById(R.id.spinnerForDoctor);
        assignLayout=view.findViewById(R.id.assignLayout);

        addCourseCodeEditText=view.findViewById(R.id.addCourseCodeEditText);
        addCourseCreditsEditText=view.findViewById(R.id.addCourseCreditsEditText);
        addCourseNameEditText=view.findViewById(R.id.addCourseNameEditText);
        addCourseCodeSubmit=view.findViewById(R.id.addCourseCodeSubmit);
       // addCourseDoctorEditText=view.findViewById(R.id.addCourseDoctorEditText);
        addCourseErrorTextView=view.findViewById(R.id.addCourseErrorTextView);

        spinnerForCourseDelete=view.findViewById(R.id.spinnerForCourseDelete);
        deleteCourseCodeSubmit=view.findViewById(R.id.deleteCourseCodeSubmit);

        doctors.clear();
        courses.clear();
        c.clear();

        assignCourseSubmit.setEnabled(false);

        db.collection("users").document("doctors").collection("data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    doctors.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        doctors.add(document.getString("name"));
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style,doctors);
                    spinnerForDoctor.setAdapter(adapter);
                }
            }
        });

        spinnerForDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

            String selectedItem = parent.getItemAtPosition(position).toString();


                    // Log.d("assa",selectedItem);
            db.collection("courses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    courses.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String d=document.get("doctor").toString();
                        db.collection("users").document("doctors").collection("data").document(d).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        nameD = task.getResult().getString("name");
                                        if (!nameD.equals(selectedItem) && !nameD.equals("")) {
                                            courses.add(document.getId());
                                        }
                                    }
                                    db.collection("courses").whereEqualTo("doctor", 0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().size() > 0)
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if (!courses.contains(document.getId()))
                                                            courses.add(document.getId());
                                                        else
                                                            break;
                                                    }

                                                adapterCourse = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style, courses);
                                                spinnerForCourse.setAdapter(adapterCourse);
                                                if (courses.size() > 0) {
                                                    assignCourseSubmit.setEnabled(true);
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        assignCourseSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    db.collection("users").document("students").collection("data").document(fileNb).set(st);
                String c = spinnerForCourse.getSelectedItem().toString();
                String d = spinnerForDoctor.getSelectedItem().toString();

                db.collection("users").document("doctors").collection("data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("name").toString().equals(d)){
                                    idDr=Integer.parseInt(document.get("username").toString());
                                    break;
                                }
                            }
                        }
                        db.collection("courses").document(c).update("doctor",idDr);
                        assignCourseSubmit.setEnabled(false);
                        Toast.makeText(getActivity(),"Added",Toast.LENGTH_SHORT).show();
                        adapterCourse.remove((String)spinnerForCourse.getSelectedItem());
                        adapterCourse.notifyDataSetChanged();



                        if (courses.size()>0)
                            assignCourseSubmit.setEnabled(true);
                    }
                });
            }
        });

        addCourseErrorTextView.setVisibility(View.GONE);
        addCourseCodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=addCourseNameEditText.getText().toString();
                String code=addCourseCodeEditText.getText().toString();
                String credit=addCourseCreditsEditText.getText().toString();
              //  String dr=addCourseDoctorEditText.getText().toString();

                int flag = 0;
                if (code.equals("")) {
                    addCourseCodeEditText.setError("Code is Required");
                    flag = 1;
                }
                if (name.equals("")) {
                    addCourseNameEditText.setError("Name is Required");
                    flag = 1;
                }
                if (credit.equals("")) {
                    addCourseCreditsEditText.setError("Credits is Required");
                    flag = 1;
                }
            //    if (dr.equals("")) {
           //         addCourseDoctorEditText.setError("Doctor is Required");
           //         flag = 1;
            //    }
                if (flag == 1)
                    return;
                String s=code.toLowerCase();
                db.collection("courses").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                             //   Toast.makeText(getActivity(),"Course Already Exists!",Toast.LENGTH_SHORT).show();
                                addCourseErrorTextView.setVisibility(View.VISIBLE);
                                addCourseErrorTextView.setText("Course Already Exists!");
                                return;
                            }
                            else{
                                    Course course=new Course(name,s,Integer.parseInt(credit),0);
                                    db.collection("courses").document(s).set(course);
                                    Toast.makeText(getActivity(),"Added",Toast.LENGTH_SHORT).show();
                                    addCourseCreditsEditText.setText("");
                                    addCourseCodeEditText.setText("");
                                    addCourseNameEditText.setText("");
                                    addCourseErrorTextView.setVisibility(View.GONE);

                                    adapterDelete.add(s);
                                    adapterDelete.notifyDataSetChanged();

                                    adapterCourse.add(s);
                                    adapterCourse.notifyDataSetChanged();

                                }
                        }
                    }
                });
            }
        });

        db.collection("courses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    c.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        c.add(document.getId());
                    }
                    adapterDelete = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style,c);
                    spinnerForCourseDelete.setAdapter(adapterDelete);
                }
            }
        });

        deleteCourseCodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = spinnerForCourseDelete.getSelectedItem().toString();
                if (!selectedItem.equals("")){
                    db.collection("courses").document(selectedItem).delete();
                    Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT).show();
                    adapterDelete.remove(selectedItem);
                    adapterDelete.notifyDataSetChanged();

                    adapterCourse.remove(selectedItem);
                    adapterCourse.notifyDataSetChanged();
                }
            }
        });


        return  view;
    }
}
