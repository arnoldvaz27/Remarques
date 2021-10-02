package com.arnoldvaz27.remarques.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.arnoldvaz27.remarques.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT=2000;
    private String lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorNoteColor9));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorNoteColor9));
        setContentView(R.layout.splash_screen);

        askPermission();


    }

    private void checkLock() {
        SharedPreferences LayoutFull = getSharedPreferences("Lock", MODE_PRIVATE);
        lock = LayoutFull.getString("appLocking", null);

        final SharedPreferences LayoutFull1 = getSharedPreferences("layoutFull", MODE_PRIVATE);
        final SharedPreferences.Editor LayoutFull2 = LayoutFull1.edit();
        if (lock == null) {
            LayoutFull2.putString("appLocking", "No");
        }
        LayoutFull2.apply();

        if(lock.equals("Yes")){

        }else{

        }
    }

    private void askPermission() {
        Dexter.withContext(SplashScreen.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA,Manifest.permission.INTERNET).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
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

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


}