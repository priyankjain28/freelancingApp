package com.peeru.task.freelancingapp.ui.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.data.dao.MyFragmentListenerImpl;
import com.peeru.task.freelancingapp.data.model.Task;
import com.peeru.task.freelancingapp.databinding.FragmentTaskListBinding;
import com.peeru.task.freelancingapp.init.AppController;
import com.peeru.task.freelancingapp.ui.activity.PartnerScreen;
import com.peeru.task.freelancingapp.ui.adapter.WorkerTaskList;
import com.peeru.task.freelancingapp.ui.push.SharedPrefManager;
import com.peeru.task.freelancingapp.ui.viewmodel.LoginSignUpViewModel;
import com.peeru.task.freelancingapp.ui.viewmodel.UserViewModel;
import com.peeru.task.freelancingapp.util.DateComparator;
import com.peeru.task.freelancingapp.util.DistanceComparator;

import java.util.Collections;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Priyank Jain on 08-10-2018.
 */
public class TaskListFragment extends Fragment {

    //region Variable Declaration
    private static final String TAG = "TaskListFragment";
    private FragmentTaskListBinding mBinding;
    private UserViewModel mModel;
    private MyFragmentListenerImpl mFragmentCallback;
    private WorkerTaskList workerTaskList;
    private PartnerScreen partnerScreen;
    private LocationManager locationManager;
    private Location location;

    //endregion

    //region Fragment Lifecycle Component
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workerTaskList = new WorkerTaskList(getContext(),getActivity());
        mModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        FirebaseApp.initializeApp(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_list,
                container, false);
        intializingView();
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        partnerScreen = (PartnerScreen) context;
        Log.d(TAG, "Peeru on attach");
        try {
            // mFragmentCallback = (MyFragmentListenerImpl) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateLoginDetail();
        mBinding.recyclerview.setAdapter(workerTaskList);
        displayTaskLiveData();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    //endregion

    //region Intializing view
    private void intializingView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerview.setLayoutManager(layoutManager);
    }
    //endregion

    private void updateLoginDetail() {
        String deviceId = SharedPrefManager.getInstance(AppController.getAppContext()).getDeviceToken();
        LoginSignUpViewModel loginViewModel = ViewModelProviders.of(getActivity()).get(LoginSignUpViewModel.class);
        location = getLastKnownLocation();
        loginViewModel.updateUserDatabase(partnerScreen.getUserName(),deviceId,location.getLatitude(),location.getLongitude());
    }
    //region Display Event List Data
    private void displayTaskLiveData() {
        location = getLastKnownLocation();
        if (mModel != null) {
            LiveData<List<Task>> liveData = mModel.getTaskListAllLiveData();
            liveData.observe(getActivity(), (List<Task> taskList) -> {
                Collections.sort(taskList, new DateComparator());
                if (taskList.size() == 0 || taskList.isEmpty()) {
                   Log.d("TAG","No Data Found");
                } else {
                    for(Task t : taskList){
                      t.setDistance(distance(t.getLat(),t.getLon(),location.getLatitude(),location.getLongitude()));
                    }
                    Collections.sort(taskList,new DistanceComparator());
                    workerTaskList.setTaskList(taskList);
                }
            });
        }
    }
    //endregion

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private Float distanceBetweenTwo(Location l1, Location l2) {
        return l1.distanceTo(l2)/1000;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return Math.round(dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}

