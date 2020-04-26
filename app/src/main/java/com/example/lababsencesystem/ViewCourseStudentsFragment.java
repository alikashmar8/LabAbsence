package com.example.lababsencesystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCourseStudentsFragment extends Fragment {

    ArrayList<String> fileNumbers;

    public ViewCourseStudentsFragment() {
        // Required empty public constructor
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txtShow;
    private ArrayList<CourseStudent> students = new ArrayList<>();
    ArrayList<String> names;
    int flag=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_student, container, false);

        txtShow=view.findViewById(R.id.txtShow);
        fileNumbers=new ArrayList<>();
        names=new ArrayList<>();

        String getCourseCode="";
        Intent intent=getActivity().getIntent();
        getCourseCode=intent.getStringExtra("CourseCode");
        txtShow.setText(getCourseCode);


        db.collection("courses").document("i3308").collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        students.add(document.toObject(CourseStudent.class));
                        fileNumbers.add(document.getId());
                        Log.d("bobm", document.toObject(CourseStudent.class).toString());
                    }
//                    loadStudents(0);
                }
            }
        });

        return view;
    }

//    private void loadStudents(final int j) {
//        if (j < fileNumbers.size()) {
//                Log.d("bobm", ";;;" + fileNumbers.get(j) + ";;;");
//                db.collection("users").document("students").collection("data").document(fileNumbers.get(j)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> taskk) {
//
//                        if (taskk.isSuccessful()) {
//                            DocumentSnapshot document = taskk.getResult();
//                            if (document.exists()) {
//                                Log.d("bobm", "DocumentSnapshot data: " + document.get("name"));
//                                loadStudents(j + 1);
//                            }
//                        }
//                    }
//                });
//        }
//    }


}
