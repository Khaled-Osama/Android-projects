package com.example.khaledosama.askme.Models;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.khaledosama.askme.AnsweredQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProfileQuestionsViewModel extends ViewModel {

    DatabaseReference PROFILE_QUESTIONS_REF = FirebaseDatabase.getInstance().getReference("profileAnsweredQuestion");

    MutableLiveData<String> userIDLiveData = new MutableLiveData<>();

    LiveData<DataSnapshot> profileQuestionsDataSnapShot = Transformations.switchMap(userIDLiveData, new Function<String, LiveData<DataSnapshot>>() {
        @Override
        public LiveData<DataSnapshot> apply(String input) {
            return new FirebaseQueryLiveData(PROFILE_QUESTIONS_REF.child(input));
        }
    });

    LiveData<List<AnsweredQuestion>> profileQuestions = Transformations.switchMap(profileQuestionsDataSnapShot, new Function<DataSnapshot, LiveData<List<AnsweredQuestion>>>() {
        @Override
        public LiveData<List<AnsweredQuestion>> apply(DataSnapshot input) {
            List<AnsweredQuestion> questions = new ArrayList<>();
            for(DataSnapshot data : input.getChildren()){
                questions.add(data.getValue(AnsweredQuestion.class));
            }
            MutableLiveData<List<AnsweredQuestion>> ret = new MutableLiveData<>();
            ret.setValue(questions);
            return ret;
        }
    });
    public void setUserID(String userID){
        userIDLiveData.setValue(userID);
    }
    public LiveData<List<AnsweredQuestion>> getProfileQuestions(){
        return profileQuestions;
    }

}
