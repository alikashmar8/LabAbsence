package com.example.lababsencesystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {

    private Student student = StudentMain.student;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText nm,un,em,pass,fnb;

    public StudentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_student_profile, container, false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        nm=view.findViewById(R.id.name);
        un=view.findViewById(R.id.username);
        em=view.findViewById(R.id.email);
        pass=view.findViewById(R.id.password);
        fnb=view.findViewById(R.id.filenb);
        Button ed = view.findViewById(R.id.Edit);


        nm.setText(student.getName());
        un.setText(student.getUsername());
        em.setText(student.getEmail());
        pass.setText(student.getPassword());
        fnb.setText(String.valueOf(student.getFileNumber()));


        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student st=new Student(nm.getText().toString(), em.getText().toString(), un.getText().toString(), pass.getText().toString(),Integer.valueOf(fnb.getText().toString()),"student");
                edit(st);
                Toast.makeText(getContext(),"Your Information had been edited",Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }

    private void edit(final Student s){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        db.collection("users").document("students")
                .collection("data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.get("email").equals(student.getEmail())) {

                           document.getReference().set(s);
                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

    }

}
