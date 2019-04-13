package com.example.khaledosama.askme.Models;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.database.MatrixCursor;
import android.util.Log;

import com.example.khaledosama.askme.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {

    private final DatabaseReference USERS_REFRENCE = FirebaseDatabase.getInstance().getReference("user");
    private FirebaseQueryLiveData usersSnapShot = new FirebaseQueryLiveData(USERS_REFRENCE);
    LiveData<List<User>> usersLiveData = Transformations.switchMap(usersSnapShot, new Function<DataSnapshot, LiveData<List<User>>>() {
        @Override
        public LiveData<List<User>> apply(DataSnapshot input) {
            ArrayList<User> users = new ArrayList();
            for(DataSnapshot dataSnapshot:input.getChildren()){
                User user = dataSnapshot.getValue(User.class);
                users.add(user);
            }
            MutableLiveData<List<User>>ret = new MutableLiveData<>();
            ret.setValue(users);
            return ret;
        }
    });

    public LiveData<List<User>> getUsers(){
        return usersLiveData;
    }

}
