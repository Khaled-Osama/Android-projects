package com.example.khaledosama.askme.Models;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.NonAnsweredQuestion;
import com.example.khaledosama.askme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PendingQuestionsViewModel extends ViewModel {

    private DatabaseReference PENDING_QUESTIONS_REFRENCE = FirebaseDatabase.getInstance().getReference("pendingQuestionsRef");

    private MutableLiveData<String> userIDLiveData = new MutableLiveData<>();

    final private LiveData<DataSnapshot> pendingQuestionsSnapShot = Transformations.switchMap(userIDLiveData,
            new Function<String, LiveData<DataSnapshot>>() {
                @Override
                public FirebaseQueryLiveData apply(String input) {

                    return new FirebaseQueryLiveData(PENDING_QUESTIONS_REFRENCE.child(input));
                }

    });
    private final LiveData<List<NonAnsweredQuestion>> pendingQuestionsLiveData = Transformations.switchMap(pendingQuestionsSnapShot,
            new Function<DataSnapshot, LiveData<List<NonAnsweredQuestion>>>() {
            @Override
            public LiveData<List<NonAnsweredQuestion>> apply(DataSnapshot input) {
                List<NonAnsweredQuestion> pendingQuestions = new ArrayList<>();
                for(DataSnapshot snapshot:input.getChildren()){
                    pendingQuestions.add(snapshot.getValue(NonAnsweredQuestion.class));

                }

                MutableLiveData<List<NonAnsweredQuestion>> mutableLiveData = new MutableLiveData<>();
                mutableLiveData.setValue(pendingQuestions);
                return mutableLiveData;
            }

    });

    public void setUserID(String userID){
        userIDLiveData.setValue(userID);
    }
    public LiveData<List<NonAnsweredQuestion>> getPendingQuestios(){
        return pendingQuestionsLiveData;
    }
}