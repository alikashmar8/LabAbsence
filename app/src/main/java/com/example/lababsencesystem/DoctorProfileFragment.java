package com.example.lababsencesystem;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorProfileFragment extends Fragment {

    Doctor doctor = DoctorMain.doctor;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText nm, un, em, pass, fnb,oldPassword,newPassword,newPasswordConfirm;
    TextView usernmaeTv,fileNumberbTv,emailTv,nameTv;
    Button ed,changePassword;
    LinearLayout linearEdit,linearShow,linearChangePassword;

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
        //pass = view.findViewById(R.id.doctorProfilePassword);
        //fnb = view.findViewById(R.id.doctorProfileFileNumber);
        ed = view.findViewById(R.id.editDoctorProfile);
        changePassword=view.findViewById(R.id.changePassword);

        usernmaeTv=view.findViewById(R.id.doctorProfileUsernametv);
        emailTv=view.findViewById(R.id.doctorProfileEmailtv);
        fileNumberbTv=view.findViewById(R.id.doctorProfileFileNumberTv);
        nameTv=view.findViewById(R.id.doctorProfilenametv);

        oldPassword=view.findViewById(R.id.oldPassword);
        newPassword=view.findViewById(R.id.newPassword);
        newPasswordConfirm=view.findViewById(R.id.newPasswordConfirm);

        linearChangePassword=view.findViewById(R.id.linearChangePassword);
        linearEdit=view.findViewById(R.id.linearEdit);
        linearShow=view.findViewById(R.id.linearShow);

        linearShow.setVisibility(View.VISIBLE);
        linearEdit.setVisibility(View.GONE);
        linearChangePassword.setVisibility(View.GONE);



        refresh();
        //pass.setText(doctor.getPassword());
        //fnb.setText(String.valueOf(doctor.getFileNumber()));

        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEditPosition=ed.getText().toString();
                if (getEditPosition.equalsIgnoreCase("Submit")){
                    int flag=0;
                    if (nm.getText().toString().equals("") ){
                        nm.setError("please enter your name");
                        flag=1;
                    }
                    if(em.getText().toString().equals("")) {
                        em.setError("please enter your email");
                        flag=1;
                    }
                    if(un.getText().toString().equals("")) {
                        un.setError("please enter your username");
                        flag=1;
                    }
                    if (flag==0) {
                        ed.setText("Edit");
                        //ed.setBackgroundColor(Color.parseColor("#FF3F51B5"));
                        ed.setBackgroundResource(R.drawable.custom_button_2);
                        linearShow.setVisibility(View.VISIBLE);
                        linearEdit.setVisibility(View.GONE);
                        changePassword.setVisibility(View.VISIBLE);
                        DoctorMain.doctor.setEmail(em.getText().toString());
                        DoctorMain.doctor.setName(nm.getText().toString());
                        DoctorMain.doctor.setUsername(un.getText().toString());
                        edit();
                        refresh();
                    }

                }
                else {
                    ed.setText("Submit");
                   // ed.setBackgroundColor(Color.parseColor("#FF4CAF50"));
                    ed.setBackgroundResource(R.drawable.custom_button);
                    linearEdit.setVisibility(View.VISIBLE);
                    linearShow.setVisibility(View.GONE);
                    changePassword.setVisibility(View.GONE);
                }

            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getPasswordPosition=changePassword.getText().toString();
                if (getPasswordPosition.equalsIgnoreCase("Submit")){
                    int flag=0;

                    if (!oldPassword.getText().toString().equals(doctor.getPassword())) {
                        oldPassword.setError("Incorrect Password");
                        flag = 1;
                    }
                    if (!newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())){
                        Toast.makeText(getActivity(),"new Password and Confirm new Password fields are different",Toast.LENGTH_LONG).show();
                        flag=1;
                    }
                    if (oldPassword.getText().length()<6) {
                        oldPassword.setError("Minimum password length must be 6");
                        flag=1;
                    }
                    if (newPassword.getText().length()<6) {
                        newPassword.setError("Minimum password length must be 6");
                        flag=1;
                    }
                    if (newPasswordConfirm.getText().length()<6) {
                        newPasswordConfirm.setError("Minimum password length must be 6");
                        flag=1;
                    }
                    if(oldPassword.getText().toString().equals("")  || oldPassword.getText().length()<6){
                        oldPassword.setError("please enter your password");
                        flag=1;
                    }
                    if(newPassword.getText().toString().equals("")){
                        newPassword.setError("please enter your password");
                        flag=1;
                    }
                    if(newPasswordConfirm.getText().toString().equals("")){
                        newPasswordConfirm.setError("please enter your password");
                        flag=1;
                    }

                    if (flag==0) {
                        changePassword.setText("Change Password");
                        //changePassword.setBackgroundColor(Color.parseColor("#FFF44336"));
                        changePassword.setBackgroundResource(R.drawable.custom_button_3);
                        linearChangePassword.setVisibility(View.GONE);
                        linearShow.setVisibility(View.VISIBLE);
                        ed.setVisibility(View.VISIBLE);
                        DoctorMain.doctor.setPassword(newPassword.getText().toString());
                        change();
                        Toast.makeText(getActivity(),"Your Password is Changed",Toast.LENGTH_SHORT).show();

                    }

                }
                else {
                    changePassword.setText("Submit");
                    //changePassword.setBackgroundColor(Color.parseColor("#FF4CAF50"));
                    changePassword.setBackgroundResource(R.drawable.custom_button);
                    linearChangePassword.setVisibility(View.VISIBLE);
                    linearShow.setVisibility(View.GONE);
                    ed.setVisibility(View.GONE);
                }
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
                "email", em.getText().toString()
        );

    }

    void change(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        db.collection("users").document("doctors")
                .collection("data").document(doctor.getFileNumber() + "").update(
                "password", newPassword.getText().toString()
        );
    }

    void refresh(){
        nm.setText(doctor.getName());
        un.setText(doctor.getUsername());
        em.setText(doctor.getEmail());

        usernmaeTv.setText("Username : "+doctor.getUsername());
        emailTv.setText("Email : "+doctor.getEmail());
        fileNumberbTv.setText("File Number : "+doctor.getFileNumber());
        nameTv.setText("Name : "+doctor.getName());
    }
}