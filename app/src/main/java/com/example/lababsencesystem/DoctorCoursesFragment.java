package com.example.lababsencesystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorCoursesFragment extends Fragment {
    Doctor doctor = DoctorMain.doctor;
    ArrayList<Course> courses = new ArrayList<>();

    public DoctorCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_courses, container, false);
        final TextView courseDr = view.findViewById(R.id.courseDr);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final RecyclerView rv = view.findViewById(R.id.doctorCoursesRecycler);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        db.collection("courses").whereEqualTo("doctor",doctor.getFileNumber()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Course course =document.toObject(Course.class);
                        courses.add(course);
//                        courseDr.setText(course.toString());
                    }
                    RecyclerView.Adapter a = new DoctorCourseAdapter(courses);
                    rv.setAdapter(a);


                }
            }
        });



        return view;

    }
}
