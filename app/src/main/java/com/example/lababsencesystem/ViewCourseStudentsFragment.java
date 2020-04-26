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


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCourseStudentsFragment extends Fragment {

    public ViewCourseStudentsFragment() {
        // Required empty public constructor
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txtShow;
    ArrayList<Integer> fileNumbers;
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
                        int fileNb = Integer.parseInt(document.getId());
                        fileNumbers.add(fileNb);
                    }

                    for (int i = 0; i < fileNumbers.size(); i++) {
                        Log.d("bobm", ";;;"+fileNumbers.get(i)+";;;");
                        db.collection("users").document("students").collection("data").document(fileNumbers.get(i).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> taskk) {
                                String name = (String) taskk.getResult().get("name");
                                if (name !=null)
                                Log.d("bobm", name);
                            }
                        });

                    }
                }
            }
        });

        return view;
    }


}
