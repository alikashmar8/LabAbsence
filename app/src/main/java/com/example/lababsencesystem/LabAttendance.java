package com.example.lababsencesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LabAttendance extends AppCompatActivity {
    Lab lab;
    ListView studentsListView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<CourseStudent> students;
    TextView noAttendanceText;
    ArrayAdapter<CourseStudent> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_attendance);

        Intent intent = getIntent();
        lab = (Lab) intent.getSerializableExtra("lab");

        studentsListView = findViewById(R.id.attendedStudentsList);
        noAttendanceText = findViewById(R.id.noAttendanceText);
        students = new ArrayList<>();

        studentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog ad = new AlertDialog.Builder(LabAttendance.this)
                        .setTitle("Attendance")
                        .setMessage("Are you sure you want to remove " + students.get(position).getName() + " from attendance ?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                db.collection("labs").document(lab.getId()).collection("attendance").document(students.get(position).getFileNumber() + "").delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        adapter.remove(adapter.getItem(position));
                                        adapter.notifyDataSetChanged();
                                        studentsListView.setAdapter(adapter);
                                        Toast.makeText(getApplicationContext(), "cStudent removed !", Toast.LENGTH_LONG).show();
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
                return false;
            }
        });

        db.collection("labs").document(lab.getId()).collection("attendance").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                students.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    CourseStudent s = document.toObject(CourseStudent.class);
                    Log.d("attstudnt", s.toString());
                    students.add(s);
                }
                if (!students.isEmpty()) {
                    noAttendanceText.setVisibility(View.GONE);
                    studentsListView.setVisibility(View.VISIBLE);
                    adapter = new ArrayAdapter(getApplicationContext(), R.layout.activity_listview, R.id.textView, students);
                    studentsListView.setAdapter(adapter);
                } else {
                    studentsListView.setVisibility(View.GONE);
                    noAttendanceText.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
