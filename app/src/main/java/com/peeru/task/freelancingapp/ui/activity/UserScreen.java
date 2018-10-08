package com.peeru.task.freelancingapp.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.data.dao.MyFragmentListenerImpl;
import com.peeru.task.freelancingapp.data.model.Task;
import com.peeru.task.freelancingapp.databinding.ActivityUserScreenBinding;
import com.peeru.task.freelancingapp.ui.fragment.NewTaskFragment;
import com.peeru.task.freelancingapp.ui.fragment.PartnerFragment;
import com.peeru.task.freelancingapp.util.Constant;

public class UserScreen extends DefaultActivity implements MyFragmentListenerImpl {

    private final NewTaskFragment newTaskFragment = new NewTaskFragment();
    private ActivityUserScreenBinding activityUserScreenBinding;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_screen);
        username = getIntent().getStringExtra(Constant.KEY_USER_NAME);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Find Cleaner");
    }

    @Override
    public void onFabButtonClicked() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_USER_NAME, username);
        bundle.putString(Constant.KEY_NAME,getName());
        newTaskFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_user, newTaskFragment)
                .addToBackStack(null)
                .commit();
    }

    public String getUserName() {
        return username;
    }

    public String getName(){
        return getIntent().getStringExtra(Constant.KEY_NAME);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }
    public void updateFragment(Task task) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_USER_NAME, getUserName());
        bundle.putString(Constant.KEY_NAME,getName());
        bundle.putString(Constant.KEY_TRANSACTION,"updateWorkerRating");

        PartnerFragment partnerFragment = new PartnerFragment(task);
        partnerFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_user, partnerFragment)
                .addToBackStack(null)
                .commit();
    }

}
