package com.example.lababsencesystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentHomeFragment extends Fragment {
    static ArrayList<Lab> labs = new ArrayList<>();
    Student student = StudentMain.student;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> coursesCode = StudentMain.coursesCode;

    public StudentHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        for (int i = 0; i < StudentMain.courses.size(); i++)
            coursesCode.add(StudentMain.courses.get(i).getCode());

        TextView helloSt = view.findViewById(R.id.helloSt);
        helloSt.setText("Hello  " + student.getName());
        final RecyclerView rv = view.findViewById(R.id.studentTodayEventsRecycler);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = sdf.format(new Date());
//        helloDr.setText(todayDate);
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date);
        labs.clear();
        db.collection("labs").whereEqualTo("date", todayDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (coursesCode.contains(document.get("course").toString())) {
                            Lab lab = document.toObject(Lab.class);
                            lab.setId(document.getId());
                            labs.add(lab);

                        }
                    }
                    RecyclerView.Adapter a = new StudentLabAdapter(labs);

                    a.notifyDataSetChanged();
                    DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
                    dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));
                    rv.addItemDecoration(dividerItemDecoration);
                    rv.setAdapter(a);
                }
            }
        });

        return view;
    }
}
