package com.example.idverifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class student extends AppCompatActivity
{

    String Role ;
    BottomNavigationView studentBnView;
    boolean checkClicked = false;

    Toolbar studentToolbar;

    DrawerLayout studentDrawerLayout;

    NavigationView studentNv;
    Bundle bundle;
    user userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Role = getIntent().getStringExtra("Role");

        bundle = getIntent().getExtras();
        userData = new user();
        if(!bundle.isEmpty())
        {
            userData.setName(bundle.getString("name"));
            userData.setEmail(bundle.getString("email"));
            userData.setId(bundle.getString("id"));
            userData.setProfilePic(bundle.getString("profilePic"));
            userData.setMobile(bundle.getString("mobile"));
            userData.setUid(bundle.getString("uid"));
            userData.setBranch(bundle.getString("branch"));
        }
        studentBnView = findViewById(R.id.studentBnView);
        studentToolbar = findViewById(R.id.studentToolBar);
        studentDrawerLayout = findViewById(R.id.studentDrawerLayout);
        studentNv = findViewById(R.id.studentNavigationView);
        //setting the toolbar / action bar for the student
        setSupportActionBar(studentToolbar);

        ActionBarDrawerToggle studentToggle = new ActionBarDrawerToggle(this,studentDrawerLayout,studentToolbar,R.string.admin_close_drawer,R.string.admin_open_drawer);
        studentDrawerLayout.addDrawerListener(studentToggle);
        studentToggle.syncState();


        //listner for student bn view
        studentBnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.bn_student_nav_home)
                {
                    loadStudentFragment(studentHome.getInstance(bundle),checkClicked);
                    return true;
                }
                else if(id == R.id.bn_student_nav_notifications)
                {
                    loadStudentFragment(new studentNotifications(),true);
                    checkClicked = true;
                    return  true;
                }
                else if(id == R.id.bn_student_nav_complaints)
                {
                    loadStudentFragment(new studentComplaints(),true);
                    checkClicked = true;
                    return  true;
                }
                else
                {
                    loadStudentFragment(userProfile.getInstance(Role,bundle),true);
                    checkClicked = true;
                    return  true;
                }
            }
        });
        studentBnView.setSelectedItemId(R.id.bn_student_nav_home);

        // side navigation for student
        studentNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//                to be completed here
                return  true;
            }
        });
    }

    public void loadStudentFragment(Fragment fragment, boolean flag)
    {
        FragmentManager adminFm = getSupportFragmentManager();
        FragmentTransaction adminFt = adminFm.beginTransaction();

        if(!flag)
        {
            adminFt.add(R.id.studentContainer,fragment);
        }
        else
            adminFt.replace(R.id.studentContainer,fragment);

        adminFt.commit();
    }
}