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

public class StudentCourseAdapter extends RecyclerView.Adapter<StudentCourseAdapter.StudentCourseViewHolder> {
    ArrayList<Course> courses;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;

    public StudentCourseAdapter(ArrayList<Course> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public StudentCourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doctor_course_card, viewGroup, false);
        StudentCourseViewHolder dcvh = new StudentCourseViewHolder(v);
        return dcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentCourseViewHolder holder, int i) {
        holder.courseCredits.setText("Credits: " + courses.get(i).credits);
        holder.courseTitle.setText("Title: " + courses.get(i).name);
        holder.courseCode.setText("Code: " + courses.get(i).code);
        holder.courseNext.setVisibility(View.GONE);
        holder.courseStudentsNumber.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class StudentCourseViewHolder extends RecyclerView.ViewHolder {

        TextView courseTitle;
        TextView courseCode;
        TextView courseCredits;
        TextView courseStudentsNumber;
        Button courseNext;

        StudentCourseViewHolder(View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.doctorCourseCardTitle);
            courseCode = itemView.findViewById(R.id.doctorCourseCardCode);
            courseNext = itemView.findViewById(R.id.doctorCourseCardNext);
            courseCredits = itemView.findViewById(R.id.doctorCourseCardCredits);
            courseStudentsNumber = itemView.findViewById(R.id.doctorCourseCardStudentsNumber);
        }
    }
}

