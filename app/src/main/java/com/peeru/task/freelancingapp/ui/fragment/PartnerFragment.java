package com.peeru.task.freelancingapp.ui.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.data.model.Task;
import com.peeru.task.freelancingapp.data.model.User;
import com.peeru.task.freelancingapp.databinding.FragmentPartnerTaskBinding;
import com.peeru.task.freelancingapp.init.AppController;
import com.peeru.task.freelancingapp.ui.push.SendPostRequest;
import com.peeru.task.freelancingapp.ui.viewmodel.LoginSignUpViewModel;
import com.peeru.task.freelancingapp.ui.viewmodel.UserViewModel;
import com.peeru.task.freelancingapp.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@SuppressLint("ValidFragment")
public class PartnerFragment extends Fragment {
    //region Variable Declaration
    private FragmentPartnerTaskBinding mBinding;
    private UserViewModel mViewModel;
    private LoginSignUpViewModel loginSignupModel;
    private String mUserName;
    private Task task;


    @SuppressLint("ValidFragment")
    public PartnerFragment(Task task) {
        this.task = task;
    }


    //endregion

    //region Fragment Lifecycle Componenet
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_partner_task, container, false);
        mBinding.setTask(task);
        initializeLayout();
        if (getArguments().get(Constant.KEY_TRANSACTION).equals("updateWorkerRating")) {
            mBinding.welcomeNote.setText(" ");
            mBinding.ratingWorkerBar.setVisibility(View.VISIBLE);
        } else {
            mBinding.welcomeNote.append(getArguments().get(Constant.KEY_NAME).toString());
        }
        setButtonClickListner();
        return mBinding.getRoot();
    }
    //endregion

    //region Intialize layout
    private void initializeLayout() {
        mUserName = getArguments().get(Constant.KEY_USER_NAME).toString();
        mViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        loginSignupModel = ViewModelProviders.of(getActivity()).get(LoginSignUpViewModel.class);
        mBinding.reject.setVisibility(View.GONE);
        switch (task.getTaskStatus()) {
            case "Open":
                mBinding.update.setText("Accepted");
                mBinding.reject.setVisibility(View.VISIBLE);
                break;
            case "Accepted":
                mBinding.update.setText("Task Start");
                break;
            case "InProgress":
                mBinding.update.setText("Complete");
                break;
            case "Completed":
            case "Rated":
                mBinding.ratingBar.setVisibility(View.VISIBLE);
                mBinding.update.setText("Submit");
                mBinding.update.setVisibility(View.VISIBLE);
                break;
        }

    }
    //endregion


    //region Submit new post
    private void setButtonClickListner() {
        mBinding.reject.setOnClickListener(view -> {
            Toast.makeText(AppController.getAppContext(),"Request Rejected",Toast.LENGTH_LONG).show();
            HashMap<String, String> hashMap = task.getTaskStatusHistory();
            String updateTime = String.valueOf(System.currentTimeMillis());
            hashMap.put("Rejected", updateTime);
            mViewModel.updateToDatabase(mUserName, "Rejected", hashMap, task.getCreatedDate(), updateTime, "N/A", "N/A");
            getActivity().onBackPressed();
        });

        mBinding.update.setOnClickListener(view -> {
            List<String> sendMsg = new ArrayList<String>();
            sendMsg.add(task.getTaskTitle() + " " + task.getTaskDescription());
            HashMap<String, String> hashMap = task.getTaskStatusHistory();
            String updateTime = String.valueOf(System.currentTimeMillis());
            switch (task.getTaskStatus()) {
                case "Open":
                    hashMap.put("Accepted", updateTime);
                    sendMsg.add("Accepted");
                    mViewModel.updateToDatabase(mUserName, "Accepted", hashMap, task.getCreatedDate(), updateTime, "N/A", "N/A");
                    break;
                case "Accepted":
                    hashMap.put("InProgress", updateTime);
                    mViewModel.updateToDatabase(mUserName, "InProgress", hashMap, task.getCreatedDate(), updateTime, "N/A", "N/A");
                    sendMsg.add("InProgress");
                    break;
                case "InProgress":
                    hashMap.put("Completed", updateTime);
                    mViewModel.updateToDatabase(mUserName, "Completed", hashMap, task.getCreatedDate(), updateTime, "N/A", "N/A");
                    sendMsg.add("Completed");
                    break;
                case "Completed":
                    String ratingBar = task.getUserRating();
                    String workerRating = task.getWorkerRating();
                    if (getArguments().get(Constant.KEY_TRANSACTION).equals("updateWorkerRating")) {
                        workerRating = String.valueOf(mBinding.ratingWorkerBar.getRating());
                    } else {
                        ratingBar = String.valueOf(mBinding.ratingBar.getRating());
                    }
                    mViewModel.updateToDatabase(mUserName, "Rated", hashMap, task.getCreatedDate(), updateTime, ratingBar, workerRating);

                    sendMsg.add("Rated");
                    break;


            }


            LiveData<User> liveData = loginSignupModel.getUserLiveData(task.getTaskOwnerId());
            liveData.observe(getActivity(), (User user) -> {
                sendMsg.add(user.getDeviceId());
                new SendPostRequest(AppController.getAppContext()).execute(sendMsg.toArray(new String[sendMsg.size()]));
            });

            getActivity().onBackPressed();
        });
    }
    //endregion

    //region Dismiss keyboard
    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && null != getActivity().getCurrentFocus())
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
    //endregion
}
