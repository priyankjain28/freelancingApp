package com.peeru.task.freelancingapp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.init.UserSession;
import com.peeru.task.freelancingapp.util.Constant;

import java.util.HashMap;

public class SplashScreen  extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    private UserSession session;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // User Session Manager
        session = new UserSession(getApplicationContext());
        sharedPreferences = getSharedPreferences(Constant.FREELANCING_APP, Context.MODE_PRIVATE);
        checkLocationPermission();

    }

    private void callHandler() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
               callIntent();
            }
        }, SPLASH_TIME_OUT);
    }

    //region Call New Post with Permission Check
    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
            callHandler();
        }
    }

    public void callIntent(){
        Intent i = new Intent(SplashScreen.this, UserLogin.class);
        if(!session.checkLogin()){
            HashMap<String, String> userDetail = session.getUserDetails();
            if(userDetail.get(UserSession.KEY_USER_ROLE).equals(Constant.PARTNER_CONSTANT))
                i=new Intent(SplashScreen.this,PartnerScreen.class);
            else
                i=new Intent(SplashScreen.this,UserScreen.class);

            i.putExtra(Constant.KEY_USER_NAME, userDetail.get(UserSession.KEY_EMAIL));
            i.putExtra(Constant.KEY_NAME, userDetail.get(UserSession.KEY_NAME));
        }
        startActivity(i);

        // close this activity
        finish();
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callIntent();
                } else {
                    checkLocationPermission();
                }
                return;
            }
        }
    }
    //endregion
}

