package com.example.lababsencesystem;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    Button login;
    EditText username,password;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document("students")
                        .collection("data")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(username.getText().toString())){

                                    if(document.get("password").equals(password.getText().toString())){
                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                        Log.d("TAG", document.getId() + " => " + document.get("email").toString());

                                        firebaseAuth.signInWithEmailAndPassword(document.get("email").toString(),document.get("password").toString()).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete( Task<AuthResult> task) {
                                                if(task.isSuccessful()) {
                                                    Log.d("TAGlogin", "task success");

                                                    Intent i = new Intent(MainActivity.this,StudentMain.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                    finish();
                                                }else {
                                                    Toast.makeText(getApplicationContext(),"not logged in",Toast.LENGTH_LONG).show();
                                                    Log.d("TAGlogin", "login failed");

                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("TAGlogin", e.getMessage());
                                            }});

                            }
                        } }


                        }else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });
    }
}
