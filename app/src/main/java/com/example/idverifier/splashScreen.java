package com.example.idverifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splashScreen extends AppCompatActivity {

    LottieAnimationView scanAnimation;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();
        scanAnimation = findViewById(R.id.scanAnimation);

        scanAnimation.animate().translationX(-2000).setDuration(500).setStartDelay(3280);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotToNext();
            }
        },3600);
    }

    private void gotToNext()
    {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            //checking if an administrator
            FirebaseDatabase.getInstance().getReference().child("Users").child("Administrator").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.hasChild(auth.getCurrentUser().getUid()))
                    {
                        String uid= auth.getCurrentUser().getUid();
                        Bundle bundle = getUserDataBundle(snapshot,uid);
                        Intent intent = new Intent(splashScreen.this,admin.class);
                        intent.putExtras(bundle);
                        intent.putExtra("Role","Administrator");
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            // checking if a Security
            FirebaseDatabase.getInstance().getReference().child("Users").child("Security").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(auth.getCurrentUser().getUid()))
                    {
                        String uid= auth.getCurrentUser().getUid();
                        Bundle bundle = getUserDataBundle(snapshot,uid);
                        Intent intent = new Intent(splashScreen.this,security.class);
                        intent.putExtras(bundle);
                        intent.putExtra("Role","Security");
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("Users").child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(auth.getCurrentUser().getUid()))
                    {
                        String uid= auth.getCurrentUser().getUid();
                        Bundle bundle = getUserDataBundle(snapshot,uid);
                        Intent intent = new Intent(splashScreen.this,student.class);
                        intent.putExtras(bundle);
                        intent.putExtra("Role","Student");
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Intent intent = new Intent(splashScreen.this,loginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public Bundle getUserDataBundle(DataSnapshot snapshot,String uid)
    {
        Bundle bundle = new Bundle();
        bundle.putString("name",snapshot.child(uid).child("name").getValue(String.class));
        bundle.putString("email",snapshot.child(uid).child("email").getValue(String.class));
        bundle.putString("id",snapshot.child(uid).child("id").getValue(String.class));
        bundle.putString("profilePic",snapshot.child(uid).child("profilePic").getValue(String.class));
        bundle.putString("mobile",snapshot.child(uid).child("mobile").getValue(String.class));
        bundle.putString("uid",snapshot.child(uid).child("uid").getValue(String.class));
        bundle.putString("branch",snapshot.child(uid).child("branch").getValue(String.class));

        return bundle;
    }
}