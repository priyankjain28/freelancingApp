package com.peeru.task.freelancingapp.ui.fragment;


import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.data.dao.MyFragmentListenerImpl;
import com.peeru.task.freelancingapp.data.model.Task;
import com.peeru.task.freelancingapp.data.model.User;
import com.peeru.task.freelancingapp.databinding.FragmentUserProfileBinding;
import com.peeru.task.freelancingapp.init.AppController;
import com.peeru.task.freelancingapp.ui.activity.UserScreen;
import com.peeru.task.freelancingapp.ui.adapter.NewTaskAdapter;
import com.peeru.task.freelancingapp.ui.push.SharedPrefManager;
import com.peeru.task.freelancingapp.ui.viewmodel.LoginSignUpViewModel;
import com.peeru.task.freelancingapp.ui.viewmodel.UserViewModel;
import com.peeru.task.freelancingapp.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static android.content.Context.LOCATION_SERVICE;

public class UserFragment extends Fragment implements OnMapReadyCallback {

    //region Variable declaration
    private FragmentUserProfileBinding fragmentUserProfileBinding;
    private UserViewModel userViewModel;
    private LoginSignUpViewModel loginViewModel;
    private MyFragmentListenerImpl mFragmentCallback;
    private SupportMapFragment mapFragment;

   // private EventListViewModel eventModel;
    private GoogleMap mMap;
    private NewTaskAdapter newTaskAdapter;
    private UserScreen userScreenActivity;
    private List<LatLng> latLngs;
    private int counter = 0;
    private HashMap<String, User> userHashMap = new HashMap<String, User>();
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    //endregion
    private LocationManager locationManager;
    private Location location;

    //region Fragment Lifecycle component
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newTaskAdapter = new NewTaskAdapter(UserFragment.this);
        userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginSignUpViewModel.class);
        location = getLastKnownLocation();
        //eventModel = ViewModelProviders.of(getActivity()).get(EventListViewModel.class);
        FirebaseApp.initializeApp(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentUserProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intializingView();
        return fragmentUserProfileBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userScreenActivity = (UserScreen) context;
        try {
            mFragmentCallback = (MyFragmentListenerImpl) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    private void updateLoginDetail() {
        String deviceId = SharedPrefManager.getInstance(AppController.getAppContext()).getDeviceToken();
        loginViewModel.updateUserDatabase(userScreenActivity.getUserName(),deviceId,location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateLoginDetail();
        fragmentUserProfileBinding.recyclerviewChild.setAdapter(newTaskAdapter);
        setupDataAndMapView();
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

    //region Map setup and refresh
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location location= getLastKnownLocation();
        LatLng initialLocation = new LatLng(location.getLatitude(),location.getLatitude());
        mMap.addMarker(new MarkerOptions().position(initialLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation,15.0f));
    }



    //region Initializing view on Fragment
    private void intializingView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentUserProfileBinding.recyclerviewChild.setLayoutManager(linearLayoutManager);
        fragmentUserProfileBinding.fabChild.setOnClickListener(v -> {
            mFragmentCallback.onFabButtonClicked();
        });

    }
    //endregion

    //region Set data on recycler view and map
    public void setupDataAndMapView() {
        // Update the list when the data changes
        if (userViewModel != null) {
            LiveData<List<Task>> liveData = userViewModel.getTaskListLiveData(userScreenActivity.getUserName());
            liveData.observe(getActivity(), (List<Task> taskList) -> {
                if (taskList != null && !taskList.isEmpty()) {
                    newTaskAdapter.setTaskList(taskList, userScreenActivity.getUserName());
                    counter = 0;
                    displayDataOnMap();
                } else {
                    //fragmentUserProfileBinding.childrenText.setText("\"No Task Found\"");
                    taskList.clear();
                    newTaskAdapter.setTaskList(taskList, userScreenActivity.getUserName());
                }
            });
        }
    }
    //endregion



    //region Worker on map view

    public void displayDataOnMap() {
        latLngs = new ArrayList<LatLng>();
        if (userViewModel != null) {
            LiveData<List<User>> liveData = userViewModel.getAllWorkerList();
            liveData.observe(getActivity(), (List<User> users) -> {
                if (users.size() != 0) {
                    int color = new Random().nextInt(360);

                    for (User u : users) {

                            LatLng position = new LatLng(u.getLat(), u.getLon());
                            latLngs.add(position);
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    .title(u.getFirstName() + " " + u.getLastName() + " " + u.getRating())
                                    .snippet(Utility.dateTimeCoversion(u.getLastUpdate()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(color))
                            );
                            marker.showInfoWindow();

                    }
                }
                cameraUpdateCall(latLngs);
            });
        }
    }
    //endregion

    // region Delete Record with Alert message
    public void deleteRecord(Task task) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setMessage("Are you sure you want to delete?");
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#56C98F'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        userViewModel.deleteRecord(task.getCreatedDate());
                        newTaskAdapter.notifyDataSetChanged();
                        //setupDataAndMapView();
                    }
                });

        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#56C98F'>No</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    //region Track Location
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
    //endregion

    //region Camera Bound with Area
    public void cameraUpdateCall(List<LatLng> latLngs) {
        Location location = getLastKnownLocation();
        LatLng p = new LatLng(location.getLatitude(), location.getLongitude());
        latLngs.add(p);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(p)
                .title("You are here")
        );
        marker.showInfoWindow();
        if (latLngs.size() > 0) {
            builder = new LatLngBounds.Builder();
            for (LatLng position : latLngs)
                builder.include(position);
            if (areBoundsTooSmall(builder.build(), 100)) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(builder.build().getCenter(), 14));
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 5));
            }
        }
    }

    private boolean areBoundsTooSmall(LatLngBounds bounds, int minDistanceInMeter) {
        float[] result = new float[1];
        Location.distanceBetween(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude, result);
        return result[0] < minDistanceInMeter;
    }

    public void updateRating(Task task) {
        ((UserScreen) getActivity()).updateFragment(task);
    }
    //endregion
}
