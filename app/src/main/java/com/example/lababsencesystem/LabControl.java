package com.example.lababsencesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class LabControl extends AppCompatActivity {
    Lab lab;
    TextView labTitle, labDateAndTime, labAttendance;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int count = 0, attendance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_control);

        Intent intent = getIntent();
        lab = (Lab) intent.getSerializableExtra("lab");

        labDateAndTime = findViewById(R.id.dateAndTime);
        labTitle = findViewById(R.id.labControlTitle);
        labAttendance = findViewById(R.id.labAttendance);

        labTitle.setText("Lab: " + lab.getCourse());
        labDateAndTime.setText(lab.getDate() + "  " + lab.getTime());
        Log.d("labbbbb", lab.toString());

        db.collection("courses").document(lab.getCourse()).collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    count++;
                }
                loadAttendance();
            }
        });


    }

    private void loadAttendance() {
        db.collection("labs").document(lab.getId()).collection("attendance").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    attendance++;
                }
                labAttendance.setText("Attendance: " + attendance + "/" + count);

            }
        });
    }
}
