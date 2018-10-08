package com.peeru.task.freelancingapp.ui.viewmodel;

import android.app.Activity;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peeru.task.freelancingapp.data.FirebaseQueryLiveData;
import com.peeru.task.freelancingapp.data.model.User;
import com.peeru.task.freelancingapp.init.AppController;
import com.peeru.task.freelancingapp.util.Constant;
import com.peeru.task.freelancingapp.util.Utility;

/**
 * Created by Priyank Jain on 08-10-2018.
 */
public class LoginSignUpViewModel extends ViewModel {
    private DatabaseReference dataRef;
    private User user = new User();
    FirebaseQueryLiveData mLiveData;
    private Boolean flag = false;

    //region User List Live data
    @NonNull
    public LiveData<User> getUserLiveData(String userName) {
        String childRef = Constant.FREELANCING_USER;
        dataRef = FirebaseDatabase.getInstance().getReference().child(childRef);
        mLiveData = new FirebaseQueryLiveData(dataRef.orderByChild("email").equalTo(userName));
        LiveData<User> mUserData = Transformations.map(mLiveData, new DeserializerUser());
        return mUserData;
    }

    private class DeserializerUser implements Function<DataSnapshot, User> {
        @Override
        public User apply(DataSnapshot dataSnapshot) {
            user = new User();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                user = snap.getValue(User.class);
            }
            return user;
        }
    }
    //endregion

    //region Create New User
    public void creatNewUser(final User user, Activity activity) {
        flag = false;
        dataRef = FirebaseDatabase.getInstance().getReference().child(Constant.FREELANCING_USER);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Boolean exist = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //If email exists then toast shows else store the data on new key
                    if (data.getValue(User.class).getEmail().equals(user.getEmail())) {
                        exist = true;
                        Toast.makeText(AppController.getAppContext(), Constant.EMAIL_EXIST, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (!exist) {
                    Toast.makeText(AppController.getAppContext(), "Store Called", Toast.LENGTH_SHORT).show();
                    dataRef.child(Utility.dotToStarConverter(user.getEmail())).setValue(user);
                    activity.onBackPressed();
                }
                flag = !exist;
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
            }
        });

    }
    //endregion

    //region Update Task
    public void updateUserDatabase(String username, String deviceId, Double lat, Double lon) {
        Log.d("TAG","Device Id"+deviceId);
        final String user = String.valueOf(username).replace(".", "*");
        // push the new message to Firebase
        dataRef = FirebaseDatabase.getInstance().getReference().child(Constant.FREELANCING_USER  + "/" + user);
        dataRef.child("deviceId").setValue(deviceId);
        dataRef.child("lat").setValue(lat);
        dataRef.child("lon").setValue(lon);
        }
    //endregion
}
