package com.example.lababsencesystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddLab extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ArrayList<String> coursesCode = new ArrayList<>();
    String date = "";
    String time = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textAddLabError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lab);
        final Spinner courseSpinner = findViewById(R.id.courseSpinner);
        for (int i = 0; i < DoctorMain.courses.size(); i++)
            coursesCode.add(DoctorMain.courses.get(i).getCode());
        ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_style, coursesCode);
        courseSpinner.setAdapter(adapter);
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final ProgressBar progressBar = findViewById(R.id.createLabProgressBar);
        Button button = findViewById(R.id.chooseDate);
        Button createLab = findViewById(R.id.createLab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        Button chooseTime = findViewById(R.id.chooseTime);
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        textAddLabError=findViewById(R.id.textAddLabError);
        createLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLab.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                textAddLabError.setVisibility(View.GONE);
                int flag=0;
                if (coursesCode.size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    textAddLabError.setText("You cannot add any lab ! \n You don't teach any course");
                    textAddLabError.setVisibility(View.VISIBLE);
                    createLab.setVisibility(View.VISIBLE);
                    return;
                }
                String s="Please Pick up a ";
                if (date.equals("") ){
                   // Toast.makeText(AddLab.this,"Please Choose a Date!",Toast.LENGTH_SHORT).show();
                    s+="date";
                    flag=1;
                }
                if (time.equals("")){
                    if (flag==1){
                        s += " and time !";
                    }
                    else{
                        s += "time !";
                        flag=1;
                    }
                }
                else
                    s+="!.";

                if (flag==1){
                    progressBar.setVisibility(View.GONE);
                    textAddLabError.setText(s);
                    textAddLabError.setVisibility(View.VISIBLE);
                    createLab.setVisibility(View.VISIBLE);
                    return;
                }

                Lab lab = new Lab(courseSpinner.getSelectedItem().toString(), date, time, DoctorMain.doctor.getFileNumber());
                db.collection("labs").add(lab).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBar.setVisibility(View.GONE);
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        view.setMinDate(System.currentTimeMillis() - 1000);

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String selectedDate = sdf.format(c.getTime());
        TextView textView = findViewById(R.id.dateShow);
        date = selectedDate;
        textView.setText(selectedDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = findViewById(R.id.showTime);
        time = hourOfDay + ":" + minute;
        textView.setText(time);
    }
}
