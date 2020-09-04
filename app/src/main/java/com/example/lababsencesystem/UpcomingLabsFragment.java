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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static android.widget.LinearLayout.HORIZONTAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingLabsFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Lab> labs = new ArrayList<>();
    Doctor doctor = DoctorMain.doctor;
    TextView text;

    public UpcomingLabsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcomming_labs, container, false);
        text = view.findViewById(R.id.noupcomingLabs);
        final RecyclerView rv = view.findViewById(R.id.upcomingLabsRV);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));
        rv.addItemDecoration(dividerItemDecoration);
        rv.setLayoutManager(lm);

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = sdf.format(new Date());
//        helloDr.setText(todayDate);


        Date today = null;
        try {
            today = sdf.parse(todayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        final Date finalToday = today;
        if (doctor != null) {
            db.collection("labs").whereEqualTo("doctor", doctor.getFileNumber()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        labs.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Lab lab = document.toObject(Lab.class);
                            lab.setId(document.getId());
                            Date labDate = null;
                            try {
                                labDate = sdf.parse(lab.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (finalToday.compareTo(labDate) < 0) {
                                labs.add(lab);
                            }
                        }
                        if (labs.size() > 0) {
                            Collections.sort(labs, new Comparator<Lab>() {
                                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

                                @Override
                                public int compare(Lab lhs, Lab rhs) {
                                    try {
                                        return f.parse(lhs.getDate()).compareTo(f.parse(rhs.getDate()));
                                    } catch (ParseException e) {
                                        throw new IllegalArgumentException(e);
                                    }
                                }
                            });
                            rv.setVisibility(View.VISIBLE);
                            text.setVisibility(View.GONE);
                            RecyclerView.Adapter a = new DoctorLabAdapter(labs);
                            a.notifyDataSetChanged();
                            rv.setAdapter(a);
                        } else {
                            rv.setVisibility(View.GONE);
                            text.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        } else {
            if (!StudentMain.coursesCode.isEmpty()) {
                db.collection("labs").whereIn("course", StudentMain.coursesCode).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            labs.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Lab lab = document.toObject(Lab.class);
                                lab.setId(document.getId());
                                Date labDate = null;
                                try {
                                    labDate = sdf.parse(lab.getDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (finalToday.compareTo(labDate) < 0) {
                                    labs.add(lab);
                                }
                            }
                            if (labs.size() > 0) {
                                Collections.sort(labs, new Comparator<Lab>() {
                                    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

                                    @Override
                                    public int compare(Lab lhs, Lab rhs) {
                                        try {
                                            return f.parse(lhs.getDate()).compareTo(f.parse(rhs.getDate()));
                                        } catch (ParseException e) {
                                            throw new IllegalArgumentException(e);
                                        }
                                    }
                                });
                                rv.setVisibility(View.VISIBLE);
                                text.setVisibility(View.GONE);
                                RecyclerView.Adapter a = new StudentLabAdapter(labs);
                                a.notifyDataSetChanged();
                                rv.setAdapter(a);
                            } else {
                                rv.setVisibility(View.GONE);
                                text.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            } else {
                rv.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
            }
        }


        return view;
    }
}
