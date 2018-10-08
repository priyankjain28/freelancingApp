package com.peeru.task.freelancingapp.ui.activity;

import android.os.Bundle;

import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.data.model.Task;
import com.peeru.task.freelancingapp.ui.fragment.PartnerFragment;
import com.peeru.task.freelancingapp.util.Constant;

public class PartnerScreen extends DefaultActivity {

    private PartnerFragment partnerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_screen);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Task List");
    }

    public void updateTask(Task task) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_USER_NAME, getUserName());
        bundle.putString(Constant.KEY_NAME,getName());
        bundle.putString(Constant.KEY_TRANSACTION,"worker");
        partnerFragment = new PartnerFragment(task);
        partnerFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, partnerFragment)
                .addToBackStack(null)
                .commit();
    }
    public String getUserName(){
        return getIntent().getStringExtra(Constant.KEY_USER_NAME);
    };
    public String getName(){
        return getIntent().getStringExtra(Constant.KEY_NAME);
    };
}
