package com.example.khaledosama.askme.Models;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.khaledosama.askme.AnsweredQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AskedQuestionsViewModel extends ViewModel {

    private final DatabaseReference ASKED_QUESTIONS_REFERENCE = FirebaseDatabase.getInstance().
            getReference("askedQuestionsRef");

    MutableLiveData<String> userIDLiveData = new MutableLiveData<>();

    final LiveData<DataSnapshot> askedQuestionsDatasnapshot = Transformations.switchMap(userIDLiveData,
            new Function<String, LiveData<DataSnapshot>>() {
                @Override
                public FirebaseQueryLiveData apply(String input) {

                    return new FirebaseQueryLiveData(ASKED_QUESTIONS_REFERENCE.child(input));
                }

            });

    final LiveData<List<AnsweredQuestion>> askedQuestionsLiveData = Transformations.switchMap(askedQuestionsDatasnapshot, new Function<DataSnapshot, LiveData<List<AnsweredQuestion>>>() {
        @Override
        public LiveData<List<AnsweredQuestion>> apply(DataSnapshot input) {
            List<AnsweredQuestion> askedQuestions = new ArrayList<>();
            for(DataSnapshot dataSnapshot: input.getChildren()){
                Log.v("WWW", dataSnapshot.getValue().toString());
                askedQuestions.add( dataSnapshot.getValue(AnsweredQuestion.class));
            }
            MutableLiveData<List<AnsweredQuestion>> mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(askedQuestions);
            return mutableLiveData;
        }
    });

    public void setUserID(String userID){
        userIDLiveData.setValue(userID);
    }

    public LiveData<List<AnsweredQuestion>> getAskedQuestions(){
        return askedQuestionsLiveData;
    }

}