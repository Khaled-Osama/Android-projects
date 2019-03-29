package com.example.khaledosama.askme.Models;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.khaledosama.askme.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileViewModel extends ViewModel {

    private final DatabaseReference USER_PROFILE_REFERENCE = FirebaseDatabase.getInstance().
            getReference("user");
    MutableLiveData<String> userIDLiveData = new MutableLiveData<>();
    LiveData<DataSnapshot> userSnapShot = Transformations.switchMap(userIDLiveData, new Function<String, LiveData<DataSnapshot>>() {
        @Override
        public LiveData<DataSnapshot> apply(String input) {
            return new FirebaseQueryLiveData(USER_PROFILE_REFERENCE.child(input));
        }
    });

    LiveData<User> userLiveData = Transformations.switchMap(userSnapShot, new Function<DataSnapshot, LiveData<User>>() {
        @Override
        public LiveData<User> apply(DataSnapshot input) {
            User user = (User)input.getValue();
            MutableLiveData<User> mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(user);
            return mutableLiveData;
        }
    });

    public void setUserID(String userID){
        userIDLiveData.setValue(userID);
    }
    public LiveData<User> getUserProfile(){
        return userLiveData;
    }
}