package com.example.lababsencesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class LabControl extends AppCompatActivity {
    Lab lab;
    TextView labTitle, labDateAndTime, labAttendance;
    Switch showHideQR;
    ImageView qrContainer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int count = 0, attendance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_control);

        Intent intent = getIntent();
        lab = (Lab) intent.getSerializableExtra("lab");

        labDateAndTime = findViewById(R.id.dateAndTime);
        labTitle = findViewById(R.id.labControlTitle);
        labAttendance = findViewById(R.id.labAttendance);
        showHideQR = findViewById(R.id.showHideQR);
        qrContainer = findViewById(R.id.qrContainer);

        labTitle.setText("Lab: " + lab.getCourse());
        labDateAndTime.setText(lab.getDate() + "  " + lab.getTime());
        Log.d("labbbbb", lab.toString());

        db.collection("courses").document(lab.getCourse()).collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    count++;
                }
                loadAttendance();
            }
        });

        showHideQR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (showHideQR.isChecked()) {
                    qrContainer.setVisibility(View.VISIBLE);
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(lab.getId(), BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qrContainer.setImageBitmap(bitmap);
//                        showHideQR.setText("Hide Attendance QR");
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {
                    qrContainer.setVisibility(View.GONE);
                }
            }


        });

    }

    private void loadAttendance() {
        db.collection("labs").document(lab.getId()).collection("attendance").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    attendance++;
                }
                labAttendance.setText("Attendance: " + attendance + "/" + count);

            }
        });
    }
}
