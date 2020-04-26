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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorProfileFragment extends Fragment {

    Doctor doctor = DoctorMain.doctor;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText nm, un, em, pass, fnb;
    Button ed;

    public DoctorProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        nm = view.findViewById(R.id.doctorProfilename);
        un = view.findViewById(R.id.doctorProfileUsername);
        em = view.findViewById(R.id.doctorProfileEmail);
        pass = view.findViewById(R.id.doctorProfilePassword);
        fnb = view.findViewById(R.id.doctorProfileFileNumber);
        ed = view.findViewById(R.id.editDoctorProfile);

        nm.setText(doctor.getName());
        un.setText(doctor.getUsername());
        em.setText(doctor.getEmail());
        pass.setText(doctor.getPassword());
        fnb.setText(String.valueOf(doctor.getFileNumber()));
        Log.d("doctorcheck",doctor.toString());
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorMain.doctor.setEmail(em.getText().toString());
                DoctorMain.doctor.setName(nm.getText().toString());
                DoctorMain.doctor.setPassword(pass.getText().toString());
                DoctorMain.doctor.setUsername(un.getText().toString());
                edit();
                Toast.makeText(getContext(),"Your Information had been edited",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    void edit() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        db.collection("users").document("doctors")
                .collection("data").document(doctor.getFileNumber() + "").update(

                "name", nm.getText().toString(),
                "username", un.getText().toString(),
                "email", em.getText().toString(),
                "password", pass.getText().toString()

        );

    }
}