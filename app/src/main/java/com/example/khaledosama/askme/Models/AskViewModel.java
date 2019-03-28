package com.example.khaledosama.askme.Models;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AskViewModel extends ViewModelProviders {

    MutableLiveData<String>userIDLiveData = new MutableLiveData<>();
    private static final DatabaseReference ASKED_QUESTIONS_REFERENCE = FirebaseDatabase.getInstance().
            getReference("askedQuestionsRef");

    final LiveData<DataSnapshot> liveData = Transformations.switchMap(userIDLiveData, new Function<String, LiveData<DataSnapshot>>() {
        @Override
        public FirebaseQueryLiveData apply(String input) {

            return new FirebaseQueryLiveData(ASKED_QUESTIONS_REFERENCE);
        }
    });
    

    public void setUserID(String userID){
        userIDLiveData.setValue(userID);
    }

    public LiveData<DataSnapshot> getAskedQuestions(){
        return liveData;
    }



}
