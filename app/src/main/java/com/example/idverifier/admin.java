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
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class admin extends AppCompatActivity
{

    String Role;
    BottomNavigationView adminBnView;
    boolean checkClicked=false;

    NavigationView adminNv;

    Toolbar adminToolbar;

    DrawerLayout adminDrawerLayout;

    Bundle bundle;

    user userData;

    Bundle sendToNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Role = getIntent().getStringExtra("Role");
        bundle = getIntent().getExtras();
        userData = new user();
        if(!bundle.isEmpty())
        {
            sendToNotifications = new Bundle();
            userData.setName(bundle.getString("name"));
            //
            sendToNotifications.putString("name",bundle.getString("name"));
            userData.setEmail(bundle.getString("email"));
            userData.setId(bundle.getString("id"));
            userData.setProfilePic(bundle.getString("profilePic"));
            userData.setMobile(bundle.getString("mobile"));
            userData.setUid(bundle.getString("uid"));
            //
            sendToNotifications.putString("uid",bundle.getString("uid"));
            userData.setBranch(bundle.getString("branch"));
        }
        adminBnView = findViewById(R.id.adminBnView);

        adminNv = findViewById(R.id.adminNavigationView);
        adminToolbar = findViewById(R.id.adminToolBar);
        adminDrawerLayout = findViewById(R.id.adminDrawerLayout);

//        setting the action bar for admin
        setSupportActionBar(adminToolbar);

        ActionBarDrawerToggle adminToggle = new ActionBarDrawerToggle(this,adminDrawerLayout,adminToolbar,R.string.admin_open_drawer,R.string.admin_close_drawer);
        adminDrawerLayout.addDrawerListener(adminToggle);
        adminToggle.syncState();


//        listner for the admin bottom navigation view
        adminBnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id = item.getItemId();
                if(id == R.id.bn_admin_nav_home)
                {
                    loadAdminFragment(new adminHome(),checkClicked);
                    return true;
                }
                else if(id == R.id.bn_admin_nav_notifications)
                {
                    loadAdminFragment(adminNotifications.getInstance(sendToNotifications),true);
                    checkClicked = true;
                    return true;
                }
                else if(id == R.id.bn_admin_nav_student_and_staffs)
                {
                    loadAdminFragment(new adminStudentAndStaff(),true);
                    checkClicked = true;
                    return true;
                }
                else
                {
                    Log.d("********check in admin Role ",Role);
                    loadAdminFragment(userProfile.getInstance(Role,bundle),true);
                    checkClicked = true;
                    return true;
                }


            }
        });
        adminBnView.setSelectedItemId(R.id.bn_admin_nav_home);
//        bottom navigation for the admin completed

//        side Navigation for the admin

        adminNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                yet to be completed later
//                dont forget
                return true;
            }
        });
    }

    public void loadAdminFragment(Fragment fragment,boolean flag)
    {
        FragmentManager adminFm = getSupportFragmentManager();
        FragmentTransaction adminFt = adminFm.beginTransaction();

        if(!flag)
        {
            adminFt.add(R.id.adminContainer,fragment);
        }
        else
            adminFt.replace(R.id.adminContainer,fragment);

        adminFt.commit();
    }
}