package com.example.lababsencesystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentCoursesFragment extends Fragment {
    ArrayList<Course> courses = StudentMain.courses;


    public StudentCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_courses, container, false);

        final RecyclerView rv = view.findViewById(R.id.studentCoursesRecycler);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        RecyclerView.Adapter a = new StudentCourseAdapter(courses);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));
        rv.addItemDecoration(dividerItemDecoration);
        rv.setAdapter(a);


        return view;
    }
}
