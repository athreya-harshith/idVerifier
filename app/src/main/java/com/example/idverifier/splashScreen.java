package com.example.idverifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class splashScreen extends AppCompatActivity {

    LottieAnimationView scanAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        scanAnimation = findViewById(R.id.scanAnimation);

        scanAnimation.animate().translationX(-2000).setDuration(1500).setStartDelay(3280);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashScreen.this,loginActivity.class);
                startActivity(intent);
                finish();
            }
        },3600);
    }
}