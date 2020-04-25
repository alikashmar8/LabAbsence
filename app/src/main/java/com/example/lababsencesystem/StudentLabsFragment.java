package com.example.lababsencesystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentLabsFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public StudentLabsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_labs, container, false);

        tabLayout = view.findViewById(R.id.tabLayoutStudent);
        viewPager = view.findViewById(R.id.PagerIdStudent);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new OldLabsFragment(), "Old Labs");
        viewPagerAdapter.addFragment(new TodayLabsFragment(), "Today Labs");
        viewPagerAdapter.addFragment(new UpcomingLabsFragment(), "Upcoming Labs");


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
