package com.example.khaledosama.askme.Models;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import com.example.khaledosama.askme.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FriendsViewModel extends ViewModel {


    private final DatabaseReference USER_FRIENDS_REFERENCE = FirebaseDatabase.getInstance().
            getReference("user").child("friends");

    MutableLiveData<String> userIDLiveData = new MutableLiveData<>();

    final LiveData<DataSnapshot> friendsDataSnapShot = Transformations.switchMap(userIDLiveData, new Function<String, LiveData<DataSnapshot>>() {
        @Override
        public LiveData<DataSnapshot> apply(String input) {
            return new FirebaseQueryLiveData(USER_FRIENDS_REFERENCE);
        }
    });

    final LiveData<ArrayList<User>>friendsLiveData = Transformations.switchMap(friendsDataSnapShot, new Function<DataSnapshot, LiveData<ArrayList<User>>>() {
        @Override
        public LiveData<ArrayList<User>> apply(DataSnapshot input) {
            ArrayList<User>friendsList = new ArrayList<>();
            for (DataSnapshot snapshot: input.getChildren()){
                friendsList.add((User)snapshot.getValue());
            }
            MutableLiveData<ArrayList<User>> mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(friendsList);
            return mutableLiveData;
        }
    });
    

    public void setUserID(String userID){
        userIDLiveData.setValue(userID);
    }


    public LiveData<ArrayList<User>> getFriends(){

        return friendsLiveData;
    }



}
