package com.example.lababsencesystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class StudentMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView studentWelcome;
    Student student;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    Preference preference=new Preference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        studentWelcome = findViewById(R.id.studetWelcome);
//        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.studentDrawer);
        navigationView = findViewById(R.id.navigationMenuStudent);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new StudentHomeFragment()).commit();
//        navigationView.setOnNavigationItemSelectedListener(navListener);
        navigationView.setNavigationItemSelectedListener(this);

        db.collection("users").document("students")
                .collection("data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d("TAG", document.getId() + "  for   => " + document.getData());
                        if (document.get("email").equals(firebaseUser.getEmail())) {
                            Log.d("TAG", "entered iff");

                            student = document.toObject(Student.class);
//                            studentWelcome.setText("Welcome  "+student.toString());

                            break;
                        }
                    }

                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
//        NavigationView.OnNavigationItemSelectedListener navListener =
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected (MenuItem menuItem){
//                        Fragment selectedFragment = null;
//                        switch (menuItem.getItemId()) {
//                            case R.id.menuStudentHome:
//                                selectedFragment = new StudentHomeFragment();
//                                break;
//                            case R.id.menuStudentProfile:
//                                selectedFragment = new StudentProfileFragment();
//                                break;
//                        }
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//                        return true;
//                    }
//                };


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int logout =0;
        Fragment selectedFragment = null;
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.menuStudentHome:
                selectedFragment = new StudentHomeFragment();
                break;
            case R.id.menuStudentProfile:
                selectedFragment = new StudentProfileFragment();
                break;
            case R.id.menuStudentLogout:
                Intent intent=new Intent(StudentMain.this,MainActivity.class);
                preference.removePreferences(StudentMain.this);
                startActivity(intent);
                logout=1;
                break;
        }
        if (logout==0)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    }
}
