package com.peeru.task.freelancingapp.ui.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.data.model.User;
import com.peeru.task.freelancingapp.databinding.FragmentLoginBinding;
import com.peeru.task.freelancingapp.init.AppController;
import com.peeru.task.freelancingapp.init.UserSession;
import com.peeru.task.freelancingapp.ui.activity.PartnerScreen;
import com.peeru.task.freelancingapp.ui.activity.UserScreen;
import com.peeru.task.freelancingapp.ui.push.SharedPrefManager;
import com.peeru.task.freelancingapp.ui.viewmodel.LoginSignUpViewModel;
import com.peeru.task.freelancingapp.util.Constant;
import com.peeru.task.freelancingapp.util.Utility;


@SuppressLint("ValidFragment")
public class Login extends Fragment {
    //region Variable Declaration
    private LoginSignUpViewModel loginSignUpViewModel;
    private UserSession session;
    private FragmentLoginBinding mBinding;
    private Intent intent;
    //endregion

    //region Fragment Lifecycle component
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,
                container, false);
        // User Session Manager
        session = new UserSession(AppController.getAppContext());
        loginSignUpViewModel = ViewModelProviders.of(getActivity()).get(LoginSignUpViewModel.class);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();
        setButtonClickListner();
    }
    //endregion

    //region Intializing View
    private void initializeView() {
        Utility.textEventListner(mBinding.username, mBinding.login);
        Utility.textEventListner(mBinding.password, mBinding.login);
    }

    private void showToastMessge(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }
    //endregion

    //region Login button to submit request
    private void setButtonClickListner() {
        mBinding.login.setOnClickListener(view -> {
            String username = mBinding.username.getText().toString();
            String password = mBinding.password.getText().toString();
            String deviceId = SharedPrefManager.getInstance(AppController.getAppContext()).getDeviceToken();

            Log.d("LOGIN","Hora Freelancing Device Id : "+deviceId);
            LiveData<User> liveData = loginSignUpViewModel.getUserLiveData(username);
            intent = new Intent(getActivity(), UserScreen.class);
            liveData.observe(getActivity(), (User user) -> {
                if (user.getPassword() != null) {
                    if (!user.getPassword().equals(password)) {
                        showToastMessge(Constant.INVALID_PASSWORD);
                        return;
                    } else if (user.getUserRole().equals(Constant.PARTNER_CONSTANT)) {
                        intent = new Intent(getContext(), PartnerScreen.class);
                    }
                    intent.putExtra(Constant.KEY_USER_NAME, username);
                    intent.putExtra(Constant.KEY_NAME, user.getFirstName());
                    session.createUserLoginSession(username, password, user.getUserRole(), user.getFirstName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    showToastMessge(Constant.USER_NOT_FOUND);
                }
            });
        });
    }
    //endregion

}
