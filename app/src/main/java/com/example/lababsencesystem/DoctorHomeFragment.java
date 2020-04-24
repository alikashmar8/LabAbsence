package com.example.lababsencesystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorHomeFragment extends Fragment {
    Doctor doctor = DoctorMain.doctor;
    TextView helloDr;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Lab> labs = new ArrayList<>();

    public DoctorHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_home, container, false);

        helloDr = view.findViewById(R.id.helloDr);
        helloDr.setText("Hello Dr. "+doctor.getName());
        final RecyclerView rv = view.findViewById(R.id.doctorTodayEventsRecycler);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = sdf.format(new Date());
//        helloDr.setText(todayDate);

        db.collection("labs").whereEqualTo("date",todayDate).whereEqualTo("doctor",doctor.getFileNumber()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int count = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        Lab lab = document.toObject(Lab.class);
                        labs.add(lab);
                    }
                    RecyclerView.Adapter a = new DoctorLabAdapter(labs);
                    rv.setAdapter(a);
                }
            }
        });
        return view;

    }
}
