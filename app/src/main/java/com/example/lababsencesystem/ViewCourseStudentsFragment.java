package com.example.lababsencesystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCourseStudentsFragment extends Fragment {


    public ViewCourseStudentsFragment() {
        // Required empty public constructor
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txtShow;
    TableLayout tableLay;
    private ArrayList<CourseStudent> students = new ArrayList<>();
    ArrayList<String> names;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_student, container, false);

        txtShow=view.findViewById(R.id.txtShow);
        tableLay=view.findViewById(R.id.tabLay);
        names=new ArrayList<>();

        String getCourseCode="";
        Intent intent=getActivity().getIntent();
        getCourseCode=intent.getStringExtra("CourseCode");
        txtShow.setText(getCourseCode);


        db.collection("courses").document(getCourseCode).collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        students.add(document.toObject(CourseStudent.class));
                    }
                    for (int i = 0; i < students.size(); i++) {
                        TableRow tr_head = new TableRow(getActivity());
                        tr_head.setBackgroundColor(Color.WHITE);// part1
                        TableRow.LayoutParams params=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
                        params.setMargins(1,1,1,1);
                        tr_head.setLayoutParams(params);
                        tr_head.setGravity(Gravity.CENTER);
                        tr_head.setBackgroundResource(R.drawable.row_border);

                        TextView label_hello = new TextView(getActivity());
                        label_hello.setText(students.get(i).getFileNumber()+"");
                        label_hello.setTextSize(18);
                        label_hello.setTextColor(Color.BLACK);          // part2
                        label_hello.setPadding(5, 5, 5, 5);
                        tr_head.addView(label_hello);// add the column to the table row here

                        TextView label_android = new TextView(getActivity());    // part3
                        label_android.setText(students.get(i).getName()+""); // set the text for the header
                        label_android.setTextColor(Color.BLACK); // set the color
                        label_android.setTextSize(18);
                        label_android.setPadding(5, 5, 5, 5); // set the padding (if required)
                        tr_head.addView(label_android);
                        tableLay.addView(tr_head, new TableLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,                    //part4
                                ViewGroup.LayoutParams.MATCH_PARENT));
                    }

                }
            }
        });

        return view;
    }

}
