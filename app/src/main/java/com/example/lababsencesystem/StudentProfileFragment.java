package com.example.lababsencesystem;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.spark.submitbutton.SubmitButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {

    private Student student = StudentMain.student;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText nm,un,em,pass,fnb,oldPassword,newPassword,newPasswordConfirm;
    TextView usernmaeTv,fileNumberbTv,emailTv,nameTv;
    Button ed,changePassword;
    LinearLayout linearEdit,linearShow,linearChangePassword;
    int cur=0;

    public StudentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_student_profile, container, false);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        nm=view.findViewById(R.id.studentProfilename);
        un=view.findViewById(R.id.studentProfileUsername);
        em=view.findViewById(R.id.studentProfileEmail);
        //pass=view.findViewById(R.id.password);
        //fnb=view.findViewById(R.id.filenb);
         ed = view.findViewById(R.id.editStudentProfile);
         changePassword = view.findViewById(R.id.changePasswordSt);

        usernmaeTv=view.findViewById(R.id.studentProfileUsernametv);
        emailTv=view.findViewById(R.id.studentProfileEmailtv);
        fileNumberbTv=view.findViewById(R.id.studentProfileFileNumberTv);
        nameTv=view.findViewById(R.id.studentProfilenametv);

        oldPassword=view.findViewById(R.id.oldPasswordSt);
        newPassword=view.findViewById(R.id.newPasswordSt);
        newPasswordConfirm=view.findViewById(R.id.newPasswordConfirmSt);

        linearChangePassword=view.findViewById(R.id.linearChangePasswordSt);
        linearEdit=view.findViewById(R.id.linearEditSt);
        linearShow=view.findViewById(R.id.linearShowSt);

        linearShow.setVisibility(View.VISIBLE);
        linearEdit.setVisibility(View.GONE);
        linearChangePassword.setVisibility(View.GONE);

        refresh();



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
                        StudentMain.student.setEmail(em.getText().toString());
                        StudentMain.student.setName(nm.getText().toString());
                        StudentMain.student.setUsername(un.getText().toString());
                        edit();
                        refresh();
                    }

                }
                else {
                    ed.setText("Submit");
                    ed.setBackgroundResource(R.drawable.custom_button);
                    //ed.setBackgroundColor(Color.parseColor("#FF4CAF50"));
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

                    if (!oldPassword.getText().toString().equals(student.getPassword())) {
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
                        StudentMain.student.setPassword(newPassword.getText().toString());
                        change();
                        Toast.makeText(getActivity(),"Your Password is Changed",Toast.LENGTH_SHORT).show();

                    }

                }
                else {
                    changePassword.setText("Submit");
                    changePassword.setBackgroundResource(R.drawable.custom_button);
                    linearChangePassword.setVisibility(View.VISIBLE);
                    linearShow.setVisibility(View.GONE);
                    ed.setVisibility(View.GONE);
                }
            }
        });


        return view;
    }

    private void edit(){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

       /* db.collection("users").document("students")
                .collection("data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.get("email").equals(student.getEmail())) {

                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });*/
        db.collection("users").document("students")
                .collection("data").document(student.getFileNumber() + "").update(

                "name", nm.getText().toString(),
                "username", un.getText().toString(),
                "email", em.getText().toString()
        );

    }

    void change(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        db.collection("users").document("students")
                .collection("data").document(student.getFileNumber() + "").update(
                "password", newPassword.getText().toString()
        );
    }

    void refresh(){
        nm.setText(student.getName());
        un.setText(student.getUsername());
        em.setText(student.getEmail());

        usernmaeTv.setText("Username : "+student.getUsername());
        emailTv.setText("Email : "+student.getEmail());
        fileNumberbTv.setText("File Number : "+student.getFileNumber());
        nameTv.setText("Name : "+student.getName());
    }

}
