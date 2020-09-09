package com.example.lababsencesystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LabAttendance extends AppCompatActivity {
    Lab lab;
    ListView studentsListView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> students;
    TextView noAttendanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_attendance);

        Intent intent = getIntent();
        lab = (Lab) intent.getSerializableExtra("lab");

        studentsListView = findViewById(R.id.attendedStudentsList);
        noAttendanceText = findViewById(R.id.noAttendanceText);
        students = new ArrayList<>();

        db.collection("labs").document(lab.getId()).collection("attendance").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                students.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    CourseStudent s = document.toObject(CourseStudent.class);
                    Log.d("attstudnt", s.toString());
                    students.add(s.toString());
                }
                if (!students.isEmpty()) {
                    noAttendanceText.setVisibility(View.GONE);
                    studentsListView.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, R.id.textView, students);
                    studentsListView.setAdapter(adapter);
                } else {
                    studentsListView.setVisibility(View.GONE);
                    noAttendanceText.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
