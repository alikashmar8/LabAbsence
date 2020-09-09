package com.example.lababsencesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMain extends AppCompatActivity {

    public ViewPager viewPager;
    public ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayoutAdmin;
    FloatingActionButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        tabLayoutAdmin = findViewById(R.id.tabLayoutAdmin);
        viewPager = findViewById(R.id.PagerIdAdmin);
        logout = findViewById(R.id.adminLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog ad = new AlertDialog.Builder(AdminMain.this)
                        // set message, title, and icon
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to Logout ?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseAuth.getInstance().signOut();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                ad.show();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    viewPager.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AdminAddDeleteFragment(), "Users Control");
        viewPagerAdapter.addFragment(new AdminAssignCourseFragment(), "Course Management");

        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();
        tabLayoutAdmin.setupWithViewPager(viewPager);

    }
}
