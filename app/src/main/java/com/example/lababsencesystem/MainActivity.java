package com.example.lababsencesystem;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    Button login;
    EditText username, password;
    ProgressBar spinner;
    TextView loginError;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    Button showPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        spinner = findViewById(R.id.progressBar1);
        loginError = findViewById(R.id.loginError);
        showPass=findViewById(R.id.showPass);
        login.setOnClickListener(new View.OnClickListener() {
            private int found = 0;

            @Override
            public void onClick(View v) {
                loginError.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                final String e = username.getText().toString();
                final String p = password.getText().toString();
                if (e.isEmpty()) {
                    username.setError("Please enter your username");
                    username.requestFocus();
                    spinner.setVisibility(View.GONE);
                } else {
                    if (p.isEmpty()) {
                        password.setError("please add your password");
                        password.requestFocus();
                        spinner.setVisibility(View.GONE);
                    } else {
                        if (p.length() < 6) {
                            password.setError("Minimum password length must be 6");
                            password.requestFocus();
                            spinner.setVisibility(View.GONE);
                        } else {
                            db.collection("users").document("students")
                                    .collection("data")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d("TAG", document.getId() + "  for   => " + document.getData());
                                            if (document.get("username").equals(username.getText().toString())) {
                                                found = 1;
                                                Log.d("TAG", document.getId() + " ifpass  => " + document.getData());
                                                firebaseAuth.signInWithEmailAndPassword(document.get("email").toString(), password.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            Intent i = new Intent(MainActivity.this, StudentMain.class);
                                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                                            spinner.setVisibility(View.GONE);
                                                            loginError.setText("Error Password Incorrect");
                                                            loginError.setVisibility(View.VISIBLE);
                                                        }

                                                    }
                                                });
                                            }
                                        }

                                    } else {
                                        Log.d("TAG", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                         if(found == 0){
                            db.collection("users").document("doctors")
                                    .collection("data")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d("TAG", document.getId() + "  for   => " + document.getData());
                                            if (document.get("username").equals(username.getText().toString())) {
                                                found = 1;

                                                Log.d("TAG", document.getId() + " ifpass  => " + document.getData());
                                                firebaseAuth.signInWithEmailAndPassword(document.get("email").toString(), password.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            Intent i = new Intent(MainActivity.this, DoctorMain.class);
                                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                                            spinner.setVisibility(View.GONE);
                                                            loginError.setText("Error Password Incorrect");
                                                            loginError.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                        if (found == 0) {
                                            loginError.setText("Error Username not found");
                                            loginError.setVisibility(View.VISIBLE);
                                            spinner.setVisibility(View.GONE);
                                        }
                                    } else {
                                         Log.d("TAG", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                        }
                        }
                    }
                }

            }
        });


        showPass.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });

    }

}
