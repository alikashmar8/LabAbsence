package com.example.lababsencesystem;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    ProgressBar attendanceProgressBar;
    TextView qrErrorText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code_scaner);

        scannerView = findViewById(R.id.zxscan);
        attendanceProgressBar = findViewById(R.id.attendanceProgressBar);
        qrErrorText = findViewById(R.id.QrErrorText);

        //noinspection deprecation
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                scannerView.setResultHandler(QRCodeScanner.this);
                scannerView.startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getApplicationContext(), "YOU MUST GRANT PERMISSION", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();


    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void handleResult(Result result) {
        attendanceProgressBar.setVisibility(View.VISIBLE);

        Map<String, Object> data = new HashMap<>();
        data.put("name", StudentMain.student.getName());
        data.put("fileNumber", StudentMain.student.getFileNumber());

        //check if lab exists
        db.collection("labs").document(result.getText()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Lab lab = task.getResult().toObject(Lab.class);
                        //check if student enrolled in this course
                        db.collection("courses").document(lab.getCourse()).collection("students").document(StudentMain.student.getFileNumber() + "").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    //student is enrolled in this course
                                    if (document.exists()) {
                                        //check if student aleady took attendance
                                        db.collection("labs").document(result.getText()).collection("attendance").document(StudentMain.student.getFileNumber() + "").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    //student did not take attendance before
                                                    if (!document.exists()) {
                                                        //take attendance
                                                        db.collection("labs").document(result.getText()).collection("attendance").document(StudentMain.student.getFileNumber() + "").set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
//                                                                Log.d("QR", "Attendance taken!");
//                                                                Toast.makeText(getApplicationContext(), "Attendance Taken", Toast.LENGTH_SHORT);
                                                                qrErrorText.setText("Attendance taken Successfully!");
                                                                attendanceProgressBar.setVisibility(View.GONE);
                                                                qrErrorText.setVisibility(View.VISIBLE);

//                                                                startActivity(new Intent(getApplicationContext(), StudentMain.class));
//                                                                finish();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
//                                                                Toast.makeText(getApplicationContext(), "Error taking attendance try again later !", Toast.LENGTH_LONG).show();
                                                                qrErrorText.setText("Error taking attendance try again later !");
                                                                attendanceProgressBar.setVisibility(View.GONE);
                                                                qrErrorText.setVisibility(View.VISIBLE);
//                                                                startActivity(new Intent(getApplicationContext(), StudentMain.class));
//                                                                finish();
                                                            }
                                                        });
                                                    } else {
                                                        qrErrorText.setText("You have already taken attendance !");
                                                        attendanceProgressBar.setVisibility(View.GONE);
                                                        qrErrorText.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            }
                                        });

                                    } else {
                                        //student not enrolled in this course
                                        qrErrorText.setText("You are not enrolled in this course/lab !");
                                        attendanceProgressBar.setVisibility(View.GONE);
                                        qrErrorText.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                    } else {
//                        Log.d("scannn","qr doesn't belong");
                        //lab not found || qr code error
                        qrErrorText.setText("QRCode Error");
                        attendanceProgressBar.setVisibility(View.GONE);
                        qrErrorText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }
}
