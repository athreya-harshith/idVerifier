package com.example.idverifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class security extends AppCompatActivity {

    String Role;
    BottomNavigationView securityBnView;

    NavigationView securityNv;

    Toolbar securityToolbar;

    DrawerLayout securityDrawerLayout;

    Bundle bundle;
    user userData;
    boolean checkClick = false;// this is for avoiding the overlapping of the homeFragment with others
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

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
//        code start from here
        securityBnView = findViewById(R.id.securityBnView);

        securityNv = findViewById(R.id.securityNavigationView);
        securityDrawerLayout = findViewById(R.id.securityDrawerLayout);
        securityToolbar = findViewById(R.id.securityToolBar);

        //setting the toolbar for the security
        setSupportActionBar(securityToolbar);

        ActionBarDrawerToggle securityToggle = new ActionBarDrawerToggle(this,securityDrawerLayout,securityToolbar,R.string.security_open_drawer,R.string.security_close_drawer);
        securityDrawerLayout.addDrawerListener(securityToggle);
        securityToggle.syncState();


        //adding listner to security Bottom Navigation
        securityBnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id = item.getItemId();
                if(id == R.id.bn_security_nav_home)
                {

//                    loadSecurityFragment(new securityHome(),false);
                    loadSecurityFragment(securityHome.getInstance(bundle),checkClick);
                    return true;
                }
                else if(id == R.id.bn_security_nav_notifications)
                {
                    loadSecurityFragment(new securityNotifications(),true);
                    checkClick = true;
                    return true;
                }
                else
                {
                    loadSecurityFragment(userProfile.getInstance(Role,bundle),true);
                    checkClick = true;
                    return  true;
                }
            }
        });
        securityBnView.setSelectedItemId(R.id.bn_security_nav_home);
        //Bottom Navigation completes for security

        //side Navigation for security

        securityNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nv_security_home)
                {
                    //loadSecurityFragment(new securityHome(),true);
                }
                else if(id == R.id.nv_security_verified_history)
                {
                    Toast.makeText(security.this,"Getting you to Verified History", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.nv_security_profile)
                {
                    //loadSecurityFragment(new securityProfile(),true);
                    //securityBnView.setSelectedItemId(id);
                }
                else if(id == R.id.nv_security_logout)
                {
                    Toast.makeText(security.this,"Logging Out !!!!! ", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(security.this,loginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.nv_security_login)
                {
                    Toast.makeText(security.this,"Login Page", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.nv_security_contactus)
                {
                    Toast.makeText(security.this,"Thanks for Contacting We will Get Back ", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(security.this,"Thanks for Rating", Toast.LENGTH_SHORT).show();

                securityDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    //method for loading the fragments for security bottom navigation
    public void loadSecurityFragment(Fragment fragment, boolean flag)
    {
        FragmentManager securityFm = getSupportFragmentManager();
        FragmentTransaction securityFt = securityFm.beginTransaction();

        if(!flag)
        {
            securityFt.add(R.id.securityContainer,fragment);
//            securityFm.popBackStack(fragment.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //securityFm.popBackStack();
        }
        else
            securityFt.replace(R.id.securityContainer,fragment);

//        securityFt.addToBackStack(null);//on back press goes to the previous fragment
        securityFt.commit();
    }

    // handling the backpresss when side navigation is open
    @Override
    public void onBackPressed() {//we want to close the drawer if its opened when backpressed instead of closing the activity
        if(securityDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            securityDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }
}