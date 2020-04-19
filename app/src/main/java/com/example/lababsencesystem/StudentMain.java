package com.example.lababsencesystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class StudentMain extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView studentWelcome;
    Student student;
    DrawerLayout dr;
    ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        dr = findViewById(R.id.studentDrawer);
        studentWelcome = findViewById(R.id.studetWelcome);
        mToggle = new ActionBarDrawerToggle(this,dr,R.string.open,R.string.close);
        dr.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        db.collection("users").document("students")
                .collection("data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d("TAG", document.getId() + "  for   => " + document.getData());
                        if (document.get("email").equals(firebaseUser.getEmail())) {
                            Log.d("TAG", "entered iff");

                            student = document.toObject(Student.class);
                            studentWelcome.setText("Welcome  "+student.toString());

                            break;
                        }
                    }

                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }
}
