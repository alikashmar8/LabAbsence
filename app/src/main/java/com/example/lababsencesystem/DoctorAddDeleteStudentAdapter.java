package com.example.lababsencesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DoctorAddDeleteStudentAdapter extends RecyclerView.Adapter<DoctorAddDeleteStudentAdapter.DoctorAddDeleteStudentViewHolder> {

    ArrayList<Student> students;
    ArrayList<CourseStudent> cs;
    int flag;
    String getCourse;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ViewCourseStudentsFragment viewCourseStudentsFragment;
    Context context;

    public DoctorAddDeleteStudentAdapter(ArrayList<Student> students,ArrayList<CourseStudent> cs,int flag,String getCourse) {
            this.students=students;
            this.cs=cs;
            this.flag=flag;
            this.getCourse=getCourse;
        viewCourseStudentsFragment = new ViewCourseStudentsFragment();
    }

    @NonNull
    @Override
    public DoctorAddDeleteStudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.docor_add_remove_student_card, viewGroup, false);
        DoctorAddDeleteStudentViewHolder dd = new DoctorAddDeleteStudentViewHolder(v);
        context = viewGroup.getContext();
        return dd;    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorAddDeleteStudentAdapter.DoctorAddDeleteStudentViewHolder holder, final int position) {

        if(flag==1) {
            holder.filenbb.setText(cs.get(position).getFileNumber() + "");
            holder.namee.setText(cs.get(position).getName());
            holder.deletee.setVisibility(View.VISIBLE);
        } else if (flag==0){
            holder.addd.setVisibility(View.VISIBLE);
            holder.namee.setText(students.get(position).getName());
            holder.filenbb.setText(students.get(position).getFileNumber() + "");
        } else{
            if (position<cs.size()) {
                holder.deletee.setVisibility(View.VISIBLE);
                holder.namee.setText(cs.get(position).getName());
                holder.filenbb.setText(cs.get(position).getFileNumber() + "");
            } else{
                holder.addd.setVisibility(View.VISIBLE);
                holder.namee.setText(students.get(position-cs.size()).getName());
                holder.filenbb.setText(students.get(position-cs.size()).getFileNumber() + "");
            }
        }

        holder.addd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==0) {
                    CourseStudent courseStudent = new CourseStudent(students.get(position).getName(), students.get(position).getFileNumber());
                    db.collection("courses").document(getCourse).collection("students").document(courseStudent.getFileNumber() + "").set(courseStudent);
                    removest(position,-1);
//                    viewCourseStudentsFragment.UpdateCourseStudents(getCourse, context);
//                    Fragment frg = null;
//                    frg = ((FragmentActivity)context).getSupportFragmentManager().findFragmentByTag("fragment_view_student");
//                    final FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
//                    ft.detach(frg);
//                    ft.attach(frg);
//                    ft.commit();
                } else {
                    CourseStudent courseStudent = new CourseStudent(students.get(position-cs.size()).getName(), students.get(position-cs.size()).getFileNumber());
                    db.collection("courses").document(getCourse).collection("students").document(courseStudent.getFileNumber() + "").set(courseStudent);
                    removest(position,cs.size());

//                    viewCourseStudentsFragment.UpdateCourseStudents(getCourse, context);
//
//                    Fragment frg = null;
//                    frg = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("fragment_view_student");
//                    final FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
//                    ft.detach(frg);
//                    ft.attach(frg);
//                    ft.commit();
                }
            }
        });

        holder.deletee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseStudent courseStudent = new CourseStudent(cs.get(position).getName(), cs.get(position).getFileNumber());
                db.collection("courses").document(getCourse).collection("students").document(courseStudent.getFileNumber() + "").delete();
                removecs(position);
//                viewCourseStudentsFragment.UpdateCourseStudents(getCourse,context);
//
//                Fragment frg = null;
//                frg = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("fragment_view_student");
//                final FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
//                ft.detach(frg);
//                ft.attach(frg);
//                ft.commit();
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public int getItemCount() {
        if (flag==1)
            return cs.size();
        else if (flag==0)
            return students.size();
        else
            return cs.size()+students.size();
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
            deletee.setVisibility(View.GONE);
            addd.setVisibility(View.GONE);
        }
    }
    public void removecs(int position) {
        cs.remove(position);
        notifyDataSetChanged();
    }

    public void removest(int position,int y) {
        if (y==-1) {
            students.remove(position);
        }
        else {
            students.remove(position - y);
        }
        notifyDataSetChanged();
    }
}
