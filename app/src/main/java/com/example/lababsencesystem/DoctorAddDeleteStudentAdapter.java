package com.example.lababsencesystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoctorAddDeleteStudentAdapter extends RecyclerView.Adapter<DoctorAddDeleteStudentAdapter.DoctorAddDeleteStudentViewHolder> {

    ArrayList<Student> students;

    public DoctorAddDeleteStudentAdapter(ArrayList<Student> students) {
            this.students=students;
    }

    @NonNull
    @Override
    public DoctorAddDeleteStudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.docor_add_remove_student_card, viewGroup, false);
        DoctorAddDeleteStudentViewHolder dd = new DoctorAddDeleteStudentViewHolder(v);
        return dd;    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAddDeleteStudentAdapter.DoctorAddDeleteStudentViewHolder holder, int position) {
        holder.filenbb.setText(students.get(position).getFileNumber()+"");
        holder.namee.setText(students.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class DoctorAddDeleteStudentViewHolder extends RecyclerView.ViewHolder {

        TextView filenbb,namee;
        Button addd,deletee;

        DoctorAddDeleteStudentViewHolder(View itemView) {
            super(itemView);
            filenbb=itemView.findViewById(R.id.filenbb);
            namee=itemView.findViewById(R.id.namee);
            addd=itemView.findViewById(R.id.Addd);
            deletee=itemView.findViewById(R.id.deletee);
            filenbb.setText("id");
            namee.setText("id");
        }
    }
}
