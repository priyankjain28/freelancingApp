package com.peeru.task.freelancingapp.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.databinding.ActivityUserLoginBinding;
import com.peeru.task.freelancingapp.ui.fragment.Login;
import com.peeru.task.freelancingapp.ui.fragment.Signup;

public class UserLogin extends AppCompatActivity {
    BottomNavigationView bottomNav;
    ActivityUserLoginBinding userLoginBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        userLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_login);

        userLoginBinding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Login()).commit();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_login:
                            selectedFragment = new Login();
                            break;
                        case R.id.nav_signup:
                            selectedFragment = new Signup();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).addToBackStack(null).commit();

                    return true;
                }
            };
}
