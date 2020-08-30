package com.example.lababsencesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LabControl extends AppCompatActivity {
    Lab lab;
    TextView labTitle, labDateAndTime, labAttendance, attendanceError,dateL,timeL;
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

        timeL = findViewById(R.id.timeL);

        labTitle = findViewById(R.id.labControlTitle);
        labAttendance = findViewById(R.id.labAttendance);
        showHideQR = findViewById(R.id.showHideQR);
        qrContainer = findViewById(R.id.qrContainer);
        attendanceError = findViewById(R.id.attendaceError);
        dateL = findViewById(R.id.dateL);

        labTitle.setText("Lab: " + lab.getCourse());
        //labDateAndTime.setText(lab.getDate() + "  " + lab.getTime());
        dateL.setText("Date: "+lab.getDate() );
        timeL.setText("Time: "+lab.getTime());
        Log.d("labbbbb", lab.toString());

        //Get Number Of Students In Course
        db.collection("courses").document(lab.getCourse()).collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    count++;
                }
                loadAttendance();
            }
        });

        //Get Today Date
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = sdf.format(new Date());
        Date today = null;
        try {
            today = sdf.parse(todayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date labDate = null;
        try {
            labDate = sdf.parse(lab.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (today.compareTo(labDate) == 0) {
            attendanceError.setVisibility(View.GONE);
            showHideQR.setVisibility(View.VISIBLE);
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
        } else {
            if (today.compareTo(labDate) < 0) { //upcoming labs
                showHideQR.setVisibility(View.GONE);
                attendanceError.setVisibility(View.VISIBLE);
                attendanceError.setText("Cannot take attendance till lab day !");
            }
            //Old Labs
            if (today.compareTo(labDate) > 0) {
                showHideQR.setVisibility(View.GONE);
                attendanceError.setVisibility(View.VISIBLE);
                attendanceError.setText("Lab time ended can't take attendance anymore !");

            }
        }

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
