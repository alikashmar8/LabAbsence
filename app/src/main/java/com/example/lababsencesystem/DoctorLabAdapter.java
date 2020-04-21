package com.example.lababsencesystem;

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

public class DoctorLabAdapter extends RecyclerView.Adapter<DoctorLabAdapter.DoctorLabViewHolder> {
    ArrayList<Lab> labs;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public DoctorLabAdapter(ArrayList<Lab> labs) {
        this.labs = labs;
    }

    @NonNull
    @Override
    public DoctorLabAdapter.DoctorLabViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doctor_lab_card, viewGroup, false);
        DoctorLabAdapter.DoctorLabViewHolder dcvh = new DoctorLabAdapter.DoctorLabViewHolder(v);
        return dcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorLabAdapter.DoctorLabViewHolder holder, int i) {
        holder.labCourse.setText("Course: "+ labs.get(i).getCourse());
//        holder.labDoctor.setText("Title: "+ labs.get(i).doctor);
        holder.labDate.setText("Date: "+ labs.get(i).getDate());
        holder.labTime.setText("Time: "+ labs.get(i).getTime());

    }

    @Override
    public int getItemCount() {
        return labs.size();
    }

    public static class DoctorLabViewHolder extends RecyclerView.ViewHolder {

        TextView labCourse;
        TextView labDate;
        TextView labTime;
        Button labEdit;

        DoctorLabViewHolder(View itemView) {
            super(itemView);
            labCourse = itemView.findViewById(R.id.doctorLabCourse);
            labDate = itemView.findViewById(R.id.doctorLabDate);
            labTime = itemView.findViewById(R.id.doctorLabTime);
             labEdit= itemView.findViewById(R.id.doctorLabEdit);

        }
    }
}
