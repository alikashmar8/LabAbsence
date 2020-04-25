package com.example.lababsencesystem;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DoctorCourseAdapter extends RecyclerView.Adapter<DoctorCourseAdapter.DoctorCourseViewHolder> {
    ArrayList<Course> courses;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;

    public DoctorCourseAdapter(ArrayList<Course> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public DoctorCourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doctor_course_card, viewGroup, false);
        DoctorCourseViewHolder dcvh = new DoctorCourseViewHolder(v);
        return dcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorCourseViewHolder holder, int i) {
        holder.courseCredits.setText("Credits: "+courses.get(i).credits);
        holder.courseTitle.setText("Title: "+courses.get(i).name);
        holder.courseCode.setText("Code: "+courses.get(i).code);

        db.collection("courses").document(courses.get(i).code).collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int count = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        count++;
                    }
                    holder.courseStudentsNumber.setText("Number Of Students: "+count);
                }
            }
        });
        final int j=i;
        holder.courseNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), AddStudent.class);
                intent.putExtra("CourseCode",courses.get(j).getCode());
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class DoctorCourseViewHolder extends RecyclerView.ViewHolder {

        TextView courseTitle;
        TextView courseCode;
        TextView courseCredits;
        TextView courseStudentsNumber;
        Button courseNext;

        DoctorCourseViewHolder(View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.doctorCourseCardTitle);
            courseCode = itemView.findViewById(R.id.doctorCourseCardCode);
            courseNext = itemView.findViewById(R.id.doctorCourseCardNext);
            courseCredits = itemView.findViewById(R.id.doctorCourseCardCredits);
            courseStudentsNumber = itemView.findViewById(R.id.doctorCourseCardStudentsNumber);
        }
    }
}
