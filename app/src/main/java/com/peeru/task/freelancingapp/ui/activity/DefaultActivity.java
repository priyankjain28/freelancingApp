package com.peeru.task.freelancingapp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.init.UserSession;


public class DefaultActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            new UserSession(getApplicationContext()).logoutUser();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
