package com.example.lababsencesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

import static android.widget.LinearLayout.HORIZONTAL;

public class DoctorLabAdapter extends RecyclerView.Adapter<StudentLabAdapter.StudentLabViewHolder> {
    ArrayList<Lab> labs;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;


    public DoctorLabAdapter(ArrayList<Lab> labs) {
        this.labs = labs;
    }

    @NonNull
    @Override
    public StudentLabAdapter.StudentLabViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doctor_lab_card, viewGroup, false);
        StudentLabAdapter.StudentLabViewHolder dcvh = new StudentLabAdapter.StudentLabViewHolder(v);
        context = viewGroup.getContext();
        return dcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentLabAdapter.StudentLabViewHolder holder, final int i) {
        holder.labCourse.setText("Course: "+ labs.get(i).getCourse());
//        holder.labDoctor.setText("Title: "+ labs.get(i).doctor);
        holder.labDate.setText("Date: "+ labs.get(i).date.toString());
        holder.labTime.setText("Time: "+ labs.get(i).getTime());
        holder.labEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), LabControl.class);
                intent.putExtra("lab", labs.get(i));
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.labDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog ad = new AlertDialog.Builder(context)
                        // set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to Delete ?")
//                .setIcon(R.drawable.delete)

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                db.collection("labs").document(labs.get(i).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            labs.remove(i);
                                            notifyItemRemoved(i);
                                            notifyItemRangeChanged(i, labs.size());
//                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                            Toast.makeText(context, "Lab Deleted !", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Error Deleting Lab !", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                ad.show();
            }
        });

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
        TextView attended;
        TextView missed;
        Button labAttendance;
        ImageView labDelete;

        DoctorLabViewHolder(View itemView) {
            super(itemView);
            labCourse = itemView.findViewById(R.id.doctorLabCourse);
            labDate = itemView.findViewById(R.id.doctorLabDate);
            labTime = itemView.findViewById(R.id.doctorLabTime);
            labEdit = itemView.findViewById(R.id.doctorLabEdit);
            attended = itemView.findViewById(R.id.labAttended);
            missed = itemView.findViewById(R.id.labMissed);
            labDelete = itemView.findViewById(R.id.deleteLab);



        }
    }
}
