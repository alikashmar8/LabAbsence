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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<String> courses = new ArrayList<>();
    ArrayList<String> doctors = new ArrayList<>();

    ArrayAdapter<String> adapterCourse;

    String name="";
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

        doctors.clear();
        courses.clear();

        assignCourseSubmit.setEnabled(false);

        db.collection("users").document("doctors").collection("data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    doctors.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        doctors.add(document.getString("name"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style,doctors);
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
                                    if (task.getResult().exists()){
                                        name = task.getResult().getString("name");
                                        if (!name.equals(selectedItem) && !name.equals("")){
                                            courses.add(document.getId());
                                        }
                                    }
                                }
                                adapterCourse = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style,courses);
                                spinnerForCourse.setAdapter(adapterCourse);
                                if(courses.size()>0) {
                                    assignCourseSubmit.setEnabled(true);
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

        return  view;
    }
}
