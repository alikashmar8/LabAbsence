package com.example.lababsencesystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DoctorMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<Course> courses = new ArrayList<>();
    public static Doctor doctor = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView doctorWelcome;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    TextView headerName, headerEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        drawerLayout = findViewById(R.id.doctorDrawer);
        navigationView = findViewById(R.id.navigationMenuDoctor);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);
        headerName = headView.findViewById(R.id.headerName);
        headerEmail = headView.findViewById(R.id.headerEmail);


        if (doctor == null) {
            db.collection("users").document("doctors")
                    .collection("data")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Log.d("TAG", document.getId() + "  for   => " + document.getData());
                            if (document.get("email").equals(firebaseUser.getEmail())) {
                                Log.d("TAG", "entered iff");

                                doctor = document.toObject(Doctor.class);
                                loadCourses(doctor);

                                headerEmail.setText(doctor.getEmail());
                                headerName.setText(doctor.getName());
                                break;
                            }
                        }

                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }

            });
        } else {
            Log.d("TAG", "doctor else " + doctor);
            headerEmail.setText(doctor.getEmail());
            headerName.setText(doctor.getName());
            loadCourses(doctor);

        }


    }

    private void loadCourses(Doctor doctor) {
        db.collection("courses").whereEqualTo("doctor", doctor.getFileNumber()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    courses.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Course course = document.toObject(Course.class);
                        courses.add(course);
                    }
                    getSupportFragmentManager().beginTransaction().add(R.id.doctor_fragment_container, new DoctorHomeFragment()).commit();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int logout = 0;
        Fragment selectedFragment = null;
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.menuDoctorHome:
                selectedFragment = new DoctorHomeFragment();
                break;
            case R.id.menuDoctorCourses:
                selectedFragment = new DoctorCoursesFragment();
                break;
            case R.id.menuDoctorLabs:
                selectedFragment = new DoctorLabsFragment();
                break;
            case R.id.menuDoctorProfile:
                selectedFragment = new DoctorProfileFragment();
                break;
            case R.id.menuDoctorLogout:
                logout = 1;
                FirebaseAuth.getInstance().signOut();
                doctor = null;
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
        }
        if (logout == 0)
            getSupportFragmentManager().beginTransaction().replace(R.id.doctor_fragment_container, selectedFragment).commit();
        return true;
    }

}
