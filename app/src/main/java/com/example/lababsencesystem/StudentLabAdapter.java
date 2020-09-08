package com.example.lababsencesystem;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StudentLabAdapter extends RecyclerView.Adapter<StudentLabAdapter.StudentLabViewHolder> {
    ArrayList<Lab> labs;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public StudentLabAdapter(ArrayList<Lab> labs) {
        this.labs = labs;
    }

    @NonNull
    @Override
    public StudentLabViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doctor_lab_card, viewGroup, false);
        StudentLabViewHolder dcvh = new StudentLabViewHolder(v);
        return dcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentLabViewHolder holder, int i) {
        holder.labCourse.setText("Course: " + labs.get(i).getCourse());
//        holder.labDoctor.setText("Title: "+ labs.get(i).doctor);
        holder.labDate.setText("Date: " + labs.get(i).date.toString());
        holder.labTime.setText("Time: " + labs.get(i).getTime());
        holder.labEdit.setVisibility(View.GONE);
        holder.labDelete.setVisibility(View.GONE);

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = sdf.format(new Date());
        Date today = null;
        try {
            today = sdf.parse(todayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date labDate = null;
        try {
            labDate = sdf.parse(labs.get(i).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (today.compareTo(labDate) > 0) { //old Lab
            holder.attended.setVisibility(View.GONE);
            holder.missed.setVisibility(View.GONE);
            holder.loadingAttendance.setVisibility(View.VISIBLE);
            db.collection("labs").document(labs.get(i).getId()).collection("attendance").document(StudentMain.student.getFileNumber() + "").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            holder.loadingAttendance.setVisibility(View.GONE);
                            holder.missed.setVisibility(View.GONE);
                            holder.attended.setVisibility(View.VISIBLE);
                        } else {
                            holder.loadingAttendance.setVisibility(View.GONE);
                            holder.attended.setVisibility(View.GONE);
                            holder.missed.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("att", "get failed with ", task.getException());
                    }
                }
            });
        }
        if (today.compareTo(labDate) == 0) {//today lab
            holder.attended.setVisibility(View.GONE);
            holder.missed.setVisibility(View.GONE);
            holder.loadingAttendance.setVisibility(View.VISIBLE);
            db.collection("labs").document(labs.get(i).getId()).collection("attendance").document(StudentMain.student.getFileNumber() + "").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            holder.loadingAttendance.setVisibility(View.GONE);
                            holder.missed.setVisibility(View.GONE);
                            holder.attended.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("att", "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return labs.size();
    }

    public static class StudentLabViewHolder extends RecyclerView.ViewHolder {

        TextView labCourse;
        TextView labDate;
        TextView labTime;
        Button labEdit;
        TextView attended;
        TextView missed;
        TextView loadingAttendance;
        ImageView labDelete;


        StudentLabViewHolder(View itemView) {
            super(itemView);
            labCourse = itemView.findViewById(R.id.doctorLabCourse);
            labDate = itemView.findViewById(R.id.doctorLabDate);
            labTime = itemView.findViewById(R.id.doctorLabTime);
            labEdit = itemView.findViewById(R.id.doctorLabEdit);
            attended = itemView.findViewById(R.id.labAttended);
            missed = itemView.findViewById(R.id.labMissed);
            loadingAttendance = itemView.findViewById(R.id.loadingAttendance);
            labDelete = itemView.findViewById(R.id.deleteLab);


        }
    }
}
