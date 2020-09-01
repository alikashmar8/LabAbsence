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

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorProfileFragment extends Fragment {

    Doctor doctor = DoctorMain.doctor;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText nm, un, em, fnb, oldPassword, newPassword, newPasswordConfirm;
    TextView nameTv;
    Button ed, changePassword, sb, sb2, cancelPasswordChange, cancelStEdit;
    LinearLayout linearEC, linearLayoutC, la;


    public DoctorProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        fnb = view.findViewById(R.id.FNEDDR);
        nm = view.findViewById(R.id.doctorProfilename);
        //    un=view.findViewById(R.id.doctorProfileUsername);
        em = view.findViewById(R.id.doctorProfileEmail);
        nameTv = view.findViewById(R.id.nameTvDr);
        oldPassword = view.findViewById(R.id.oldPasswordDr);
        newPassword = view.findViewById(R.id.newPasswordDr);
        newPasswordConfirm = view.findViewById(R.id.newPasswordConfirmDr);

        ed = view.findViewById(R.id.editDoctorProfile);
        changePassword = view.findViewById(R.id.changePasswordDr);
        sb = view.findViewById(R.id.submitProfileDr);
        linearEC = view.findViewById(R.id.layECDr);
        linearLayoutC = view.findViewById(R.id.laCDr);
        la = view.findViewById(R.id.laDr);
        sb2 = view.findViewById(R.id.submit2Dr);
        cancelStEdit = view.findViewById(R.id.cancelStEdit);

        refresh();


        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nm.setCursorVisible(true);
                nm.setFocusableInTouchMode(true);
//                un.setCursorVisible(true);
//                un.setFocusableInTouchMode(true);
                em.setCursorVisible(true);
                em.setFocusableInTouchMode(true);
                nm.setInputType(InputType.TYPE_CLASS_TEXT);
//                un.setInputType(InputType.TYPE_CLASS_TEXT);
                em.setInputType(InputType.TYPE_CLASS_TEXT);
                sb.setVisibility(View.VISIBLE);
//                cancelPasswordChange.setVisibility(View.VISIBLE);
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
        /*        if (un.getText().toString().equals("")) {
                    un.setError("please enter your username");
                    flag = 1;
                }
        */
                if (flag == 0) {
                    DoctorMain.doctor.setEmail(em.getText().toString());
                    DoctorMain.doctor.setName(nm.getText().toString());
                    //DoctorMain.doctor.setUsername(un.getText().toString());
                    firebaseUser.updateEmail(em.getText().toString());
                    edit();
                    refresh();
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
                oldPassword.setText("");
                newPassword.setText("");
                newPasswordConfirm.setText("");
            }
        });

        sb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;

                if (!oldPassword.getText().toString().equals(doctor.getPassword()) || oldPassword.getText().length() < 6) {
                    oldPassword.setError("Incorrect Password");
                    flag = 1;
                }
                if (!newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())) {
                    newPassword.setError("New password and confirm new password fields are different");
                    newPasswordConfirm.setError("New password and confirm new password fields are different");
                    flag = 1;
                }
                if (newPassword.getText().length() < 6) {
                    newPassword.setError("Minimum password length must be 6");
                    flag = 1;
                }
                if (newPasswordConfirm.getText().length() < 6) {
                    newPasswordConfirm.setError("Minimum password length must be 6");
                    flag = 1;
                }
                if (oldPassword.getText().toString().equals("") || oldPassword.getText().length() < 6) {
                    oldPassword.setError("Please enter your password");
                    flag = 1;
                }
                if (newPassword.getText().toString().equals("")) {
                    newPassword.setError("Please enter a new password");
                    flag = 1;
                }
                if (newPasswordConfirm.getText().toString().equals("")) {
                    newPasswordConfirm.setError("Please confirm your password");
                    flag = 1;
                }
                if (flag == 0) {
                    DoctorMain.doctor.setPassword(newPassword.getText().toString());
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

    void edit() {
        db.collection("users").document("doctors")
                .collection("data").document(doctor.getFileNumber() + "").update(
                "name", nm.getText().toString(),
                "email", em.getText().toString()
        );

    }

    void change() {
        db.collection("users").document("doctors")
                .collection("data").document(doctor.getFileNumber() + "").update(
                "password", newPassword.getText().toString()
        );
    }

    void refresh() {
        fnb.setText(doctor.getFileNumber() + "");
        nm.setText(doctor.getName());
//        un.setText(doctor.getUsername());
        em.setText(doctor.getEmail());
        nameTv.setText(doctor.getName());
        sb.setVisibility(View.GONE);
//        cancelPasswordChange.setVisibility(View.GONE);
        linearEC.setVisibility(View.VISIBLE);
        sb2.setVisibility(View.GONE);
        linearLayoutC.setVisibility(View.GONE);
        la.setVisibility(View.VISIBLE);

        nm.setInputType(InputType.TYPE_NULL);
//        un.setInputType(InputType.TYPE_NULL);
        em.setInputType(InputType.TYPE_NULL);

//        nm.setFocusable(false);
        //     un.setCursorVisible(false);
        //     un.setFocusable(false);
        //    un.setCursorVisible(false);
//        em.setFocusable(false);
//        em.setCursorVisible(false);
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}