package com.example.lababsencesystem;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    EditText nm, un, em, pass, fnb, oldPassword, newPassword, newPasswordConfirm;
    TextView usernmaeTv, fileNumberbTv, emailTv, nameTv;
    Button ed, changePassword, sb, sb2, cancelStEdit;
    LinearLayout linearEC, linearLayoutC, layCS, la;
    int cur = 0;
    private Student student = StudentMain.student;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public StudentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);

        fnb = view.findViewById(R.id.FNED);
        nm = view.findViewById(R.id.studentProfilename);
        //      un=view.findViewById(R.id.studentProfileUsername);
        em = view.findViewById(R.id.studentProfileEmail);
        nameTv = view.findViewById(R.id.nameTvSt);
        oldPassword = view.findViewById(R.id.oldPasswordSt);
        newPassword = view.findViewById(R.id.newPasswordSt);
        newPasswordConfirm = view.findViewById(R.id.newPasswordConfirmSt);

        ed = view.findViewById(R.id.editStudentProfile);
        changePassword = view.findViewById(R.id.changePasswordSt);
        sb = view.findViewById(R.id.submitProfileSt);
        linearEC = view.findViewById(R.id.layEC);
        linearLayoutC = view.findViewById(R.id.laC);
        la = view.findViewById(R.id.la);
        sb2 = view.findViewById(R.id.submit2);
        cancelStEdit = view.findViewById(R.id.cancelStEdit);
        //  layCS=view.findViewById(R.id.layCS);

        refresh();


        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nm.setCursorVisible(true);
                nm.setFocusableInTouchMode(true);
                //          un.setCursorVisible(true);
                //          un.setFocusableInTouchMode(true);
                em.setCursorVisible(true);
                em.setFocusableInTouchMode(true);
                nm.setInputType(InputType.TYPE_CLASS_TEXT);
                //            un.setInputType(InputType.TYPE_CLASS_TEXT);
                em.setInputType(InputType.TYPE_CLASS_TEXT);
                sb.setVisibility(View.VISIBLE);
                linearEC.setVisibility(View.GONE);
            }
        });

        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                if (!isValidEmail(em.getText().toString())) {
                    em.setError("Invalid email format");
                    flag = 1;
                }
                if (nm.getText().toString().equals("")) {
                    nm.setError("please enter your name");
                    flag = 1;
                }
                if (em.getText().toString().equals("")) {
                    em.setError("please enter your email");
                    flag = 1;
                }

                if (flag == 0) {
                    StudentMain.student.setEmail(em.getText().toString());
                    StudentMain.student.setName(nm.getText().toString());
                    //            StudentMain.student.setUsername(un.getText().toString());
                    firebaseUser.updateEmail(em.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                edit();
                            } else {
                                Toast.makeText(getContext(), "Email Already Exists !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearEC.setVisibility(View.GONE);
                sb2.setVisibility(View.VISIBLE);
                linearLayoutC.setVisibility(View.VISIBLE);
                la.setVisibility(View.GONE);
                //   layCS.setVisibility(View.VISIBLE);
                oldPassword.setText("");
                newPassword.setText("");
                newPasswordConfirm.setText("");
            }
        });

        sb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;

                if (!oldPassword.getText().toString().equals(student.getPassword()) || oldPassword.getText().length() < 6) {
                    oldPassword.setError("Incorrect Password");
                    flag = 1;
                }
                if (!newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())) {
                    newPassword.setError("New password and confirm new password fields are different");
                    newPasswordConfirm.setError("New password and confirm new password fields are different");
                    flag = 1;
                }
//                if (oldPassword.getText().length() < 6) {
//                    oldPassword.setError("Minimum password length must be 6");
//                    flag = 1;
//                }
                if (newPassword.getText().length() < 6) {
                    newPassword.setError("Minimum password length must be 6");
                    flag = 1;
                }
                if (newPasswordConfirm.getText().length() < 6) {
                    newPasswordConfirm.setError("Minimum password length must be 6");
                    flag = 1;
                }
                if (oldPassword.getText().toString().isEmpty()) {
                    oldPassword.setError("Please enter your password");
                    flag = 1;
                }
                if (newPassword.getText().toString().isEmpty()) {
                    newPassword.setError("Please enter a new password");
                    flag = 1;
                }
                if (newPasswordConfirm.getText().toString().isEmpty()) {
                    newPasswordConfirm.setError("Please confirm your password");
                    flag = 1;
                }
                if (flag == 0) {
                    StudentMain.student.setPassword(newPassword.getText().toString());
                    firebaseUser.updatePassword(newPassword.getText().toString());
                    change();
                    Toast.makeText(getActivity(), "Your Password is Changed", Toast.LENGTH_SHORT).show();
                    refresh();
                }
            }
        });

        cancelStEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearEC.setVisibility(View.VISIBLE);
                linearLayoutC.setVisibility(View.VISIBLE);
                la.setVisibility(View.VISIBLE);
                //   layCS.setVisibility(View.GONE);
                linearLayoutC.setVisibility(View.GONE);

            }
        });

        return view;
    }

    private void edit() {
        db.collection("users").document("students")
                .collection("data").document(student.getFileNumber() + "").update(
                "name", nm.getText().toString(),
                "email", em.getText().toString()
        );
        refresh();
    }

    void change() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        db.collection("users").document("students")
                .collection("data").document(student.getFileNumber() + "").update(
                "password", newPassword.getText().toString()
        );
    }

    void refresh() {
        fnb.setText(student.getFileNumber() + "");
        nm.setText(student.getName());
        //    un.setText(student.getUsername());
        em.setText(student.getEmail());
        nameTv.setText(student.getName());
        sb.setVisibility(View.GONE);
        linearEC.setVisibility(View.VISIBLE);
        sb2.setVisibility(View.GONE);
        linearLayoutC.setVisibility(View.GONE);
        la.setVisibility(View.VISIBLE);


//        nm.setInputType(InputType.TYPE_NULL);
        //     un.setInputType(InputType.TYPE_NULL);
//        em.setInputType(InputType.TYPE_NULL);

//        nm.setFocusable(false);
        //    un.setCursorVisible(false);
        //    un.setFocusable(false);
        //   un.setCursorVisible(false);
//        em.setFocusable(false);
//        em.setCursorVisible(false);


    }


    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
