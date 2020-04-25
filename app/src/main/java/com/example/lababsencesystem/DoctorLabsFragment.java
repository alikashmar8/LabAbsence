package com.example.lababsencesystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorLabsFragment extends Fragment {


    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    ViewPager viewPager;
    FloatingActionButton addLab;

    public DoctorLabsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_doctor_labs, container, false);
        tabLayout = view.findViewById(R.id.tabLay);
//        appBarLayout = view.findViewById(R.id.appBarLayout);
        viewPager = view.findViewById(R.id.PagerId);
        addLab = view.findViewById(R.id.addLab);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new OldLabsFragment(),"Old Labs");
        viewPagerAdapter.addFragment(new TodayLabsFragment(),"Today Labs");
        viewPagerAdapter.addFragment(new UpcomingLabsFragment(),"Upcoming Labs");

        addLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"pressed",Toast.LENGTH_LONG).show();
                startActivity(new Intent((getActivity()),AddLab.class));

            }
        });

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        return  view;
    }

}
