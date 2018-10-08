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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.databinding.FragmentTaskBinding;
import com.peeru.task.freelancingapp.ui.viewmodel.UserViewModel;
import com.peeru.task.freelancingapp.util.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Priyank Jain on 08-10-2018.
 */
public class NewTaskFragment extends Fragment {
    private FragmentTaskBinding fragmentTaskBinding;
    //region Varaible Declaration
    private static final String SELECT_MESSAGE = "Please select category type";
    private static final String OTHERS_VALUE = "Others";
    private UserViewModel userViewModel;
    private String mUserName;
    private LocationManager locationManager;
    private Location location;
    private ArrayAdapter<String> dataAdapter;
    private List<String> taskType = new ArrayList<String>();
    private String title, type;
    private String TAG = "NewPostFragment";
    private String transaction, createTime;
    //endregion

    //region Fragment Lifecycle component
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentTaskBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false);
        setInstruction();
        return fragmentTaskBinding.getRoot();
    }
    //endregion


    //region Set instruction
    private void setInstruction() {
        initializeLayout();
        setBindingVariableEvent();
        setAndLoadDataOnDataAdapter();
        setButtonClickListner();
    }
    //endregion

    //region Intialize Layout
    @SuppressLint("MissingPermission")
    private void initializeLayout() {
        taskType = new ArrayList<String>();
        mUserName = getArguments().getString(Constant.KEY_USER_NAME);
        transaction = getArguments().getString(Constant.KEY_TRANSACTION);
        createTime = getArguments().getString(Constant.KEY_TRANSACTION_TIME);
        userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        location = getLastKnownLocation();
    }
    //endregion

    //region Set variable and event handling
    private void setBindingVariableEvent() {
        fragmentTaskBinding.welcome.append("" + getArguments().getString(Constant.KEY_NAME));
        textEventListner(fragmentTaskBinding.titleTask);
        textEventListner(fragmentTaskBinding.taskTypeEditText);
        spinnerEventListner(fragmentTaskBinding.taskTypeSpinner);
    }
    //endregion

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

    //region Set LiveData on Adapter
    private void setAndLoadDataOnDataAdapter() {
        if (userViewModel != null) {
            LiveData<Set<String>> liveData = userViewModel.getTaskTypeList();

            liveData.observe(getActivity(), (Set<String> mEntities) -> {
                taskType.addAll(mEntities);
                taskType.add(OTHERS_VALUE);
                //eventType.notify();
            });
        }
        taskType.add(SELECT_MESSAGE);

        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, taskType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
    //endregion

    //region Send data on Firebase
    private void setButtonClickListner() {
        Log.d("ERROR","Hora Button Click Listner");

            fragmentTaskBinding.taskTypeSpinner.setAdapter(dataAdapter);
            fragmentTaskBinding.sendButton.setOnClickListener(view -> {
                if (type.equals(OTHERS_VALUE)) {
                    type = fragmentTaskBinding.taskTypeEditText.getText().toString();
                    userViewModel.createTypeAndSentToDataBase(type);
                }
                title = fragmentTaskBinding.titleTask.getText().toString();
                if (!type.equals(SELECT_MESSAGE)) {
                    storeAndUpdateDataOnDatabase();
                } else {
                    Toast.makeText(getContext(), SELECT_MESSAGE + " or add category (Others)", Toast.LENGTH_LONG).show();
                }
            });

    }

    private void storeAndUpdateDataOnDatabase() {
        userViewModel.createAndSendToDataBase(
                mUserName,
                title,
                type,
                location.getLatitude(),
                location.getLongitude(),
                String.valueOf(System.currentTimeMillis()));
        eventStoredBackToFragment();
    }

    private void eventStoredBackToFragment() {
        fragmentTaskBinding.taskTypeEditText.setText("");
        fragmentTaskBinding.titleTask.setText("");
        dismissKeyboard();
        getActivity().onBackPressed();
    }
    //endregion

    //region Spinner Listner
    private void spinnerEventListner(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = adapterView.getSelectedItem().toString();
                if (type.equals(OTHERS_VALUE)) {
                    fragmentTaskBinding.taskTypeEditText.setVisibility(View.VISIBLE);
                    fragmentTaskBinding.sendButton.setEnabled(false);
                } else if (type.equals(SELECT_MESSAGE)) {
                    fragmentTaskBinding.sendButton.setEnabled(false);
                } else {
                    fragmentTaskBinding.taskTypeEditText.setText("");
                    fragmentTaskBinding.sendButton.setEnabled(true);
                    fragmentTaskBinding.taskTypeEditText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    //endregion

    //region Edittext Listner
    private void textEventListner(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    fragmentTaskBinding.sendButton.setEnabled(true);
                } else {
                    fragmentTaskBinding.sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
    //endregion

    //region DismissKeyboard
    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && null != getActivity().getCurrentFocus())
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
    //endregion

}
