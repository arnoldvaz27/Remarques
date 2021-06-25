package com.example.remarques.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.remarques.R;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT=2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorNoteColor9));
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,
                        MainActivity.class);
                SplashScreen.this.startActivity(i);
                SplashScreen.this.finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}