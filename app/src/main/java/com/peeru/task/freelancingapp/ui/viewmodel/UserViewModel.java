package com.peeru.task.freelancingapp.ui.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.peeru.task.freelancingapp.data.FirebaseQueryLiveData;
import com.peeru.task.freelancingapp.data.model.Task;
import com.peeru.task.freelancingapp.data.model.User;
import com.peeru.task.freelancingapp.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UserViewModel  extends ViewModel {
    private DatabaseReference dataRef;
    private List<Task> mList = new ArrayList<Task>();
    private Set<String> etList = new HashSet<>();

    //region Task List Live Data User Specific
    @NonNull
    public LiveData<List<Task>> getTaskListLiveData(String user) {
        String childRef = Constant.FREELANCING_TASK;
        dataRef = FirebaseDatabase.getInstance().getReference().child(childRef);

        Query userQuery = dataRef.orderByChild("taskOwnerId").equalTo(user);

        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(userQuery);

        LiveData<List<Task>> mEventLiveData =
                Transformations.map(mLiveData, new Deserializer());

        return mEventLiveData;
    }

    @NonNull
    public LiveData<List<Task>> getTaskListAllLiveData() {
        String childRef = Constant.FREELANCING_TASK;
        dataRef = FirebaseDatabase.getInstance().getReference().child(childRef);

        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(dataRef);

        LiveData<List<Task>> mUserLiveData =
                Transformations.map(mLiveData, new Deserializer());

        return mUserLiveData;
    }

    private class Deserializer implements Function<DataSnapshot, List<Task>> {
        @Override
        public List<Task> apply(DataSnapshot dataSnapshot) {
            mList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                Task msg = snap.getValue(Task.class);
                mList.add(msg);
            }
            return mList;
        }
    }
    //endregion

    //region Task Type List
    @NonNull
    public LiveData<Set<String>> getTaskTypeList() {
        String childRef = Constant.FREELANCING_TASK_TYPE;
        dataRef = FirebaseDatabase.getInstance().getReference().child(childRef);
        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(dataRef);

        LiveData<Set<String>> mEventTypeList =
                Transformations.map(mLiveData, new DeserializerEventType());

        return mEventTypeList;
    }

    private class DeserializerEventType implements Function<DataSnapshot, Set<String>> {
        @Override
        public Set<String> apply(DataSnapshot dataSnapshot) {
            etList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                String msg = snap.getValue(String.class);
                etList.add(msg);
            }
            return etList;
        }

    }
    //endregion

    //region get All Worker List
    @NonNull
    public LiveData<List<User>> getAllWorkerList() {
        String childRef = Constant.FREELANCING_USER;
        dataRef = FirebaseDatabase.getInstance().getReference().child(childRef);

        Query userQuery = dataRef.orderByChild("userRole").equalTo("Partner");

        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(userQuery);

        LiveData<List<User>> mEventLiveData =
                Transformations.map(mLiveData, new DeserializerUser());

        return mEventLiveData;
    }

    private class DeserializerUser implements Function<DataSnapshot, List<User>> {
        @Override
        public List<User> apply(DataSnapshot dataSnapshot) {
            List<User> userList = new ArrayList<User>();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                User user = snap.getValue(User.class);
                userList.add(user);
            }
            return userList;
        }
    }
    //endregion

    //region Delete Relation
    public void deleteRecord(String created) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query userQuery = ref.child(Constant.FREELANCING_TASK+"/"+created);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userRecord : dataSnapshot.getChildren()) {
                    userRecord.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    //endregion

    //region Create new task
    public void createAndSendToDataBase(String userName, String title, String type, Double lat, Double lon, String dateTime) {
        try {
            Task entity = new Task(userName, title, type, lat, lon, dateTime);
            final String user = String.valueOf(userName).replace(".", "*");

            // push the new message to Firebase
            dataRef = FirebaseDatabase.getInstance().getReference().child(Constant.FREELANCING_TASK + "/" + dateTime);
            dataRef.setValue(entity);

            dataRef = FirebaseDatabase.getInstance().getReference().child(Constant.FREELANCING_USER + user + "/lastUpdate");
            dataRef.setValue(dateTime);
        }catch (Exception e){
            Log.d("ERROR","Error to update Task "+e.getMessage());
        }
    }
    //endregion

    //region Update Task
    public void updateToDatabase(String workerId, String taskStatus, HashMap<String,String> statusSummary, String dateTime,String updateTime,String rating,String workerRating) {
        final String user = String.valueOf(workerId).replace(".", "*");
        // push the new message to Firebase
        dataRef = FirebaseDatabase.getInstance().getReference().child(Constant.FREELANCING_TASK  + "/" + dateTime);
        dataRef.child("taskStatus").setValue(taskStatus);
        dataRef.child("taskStatusHistory").setValue(statusSummary);
        dataRef.child("taskServicerId").setValue(workerId);
        dataRef.child("modifyDate").setValue(updateTime);
        dataRef.child("userRating").setValue(rating);
        dataRef.child("workerRating").setValue(workerRating);
    }
    //endregion

    //region Create Task type
    public void createTypeAndSentToDataBase(String type) {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constant.FREELANCING_TASK_TYPE)
                    .push()
                    .setValue(type);
        }
//endregion
}