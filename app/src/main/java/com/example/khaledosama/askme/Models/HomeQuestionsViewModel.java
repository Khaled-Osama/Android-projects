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

public class HomeQuestionsViewModel extends ViewModel {

    private DatabaseReference HOME_QUESTIONS_REF = FirebaseDatabase.getInstance().getReference("homeAnsweredQuestions");
    private MutableLiveData<String> userIDLiveData = new MutableLiveData<>();
    private LiveData<DataSnapshot> homeQuestionsDataSnapShot = Transformations.switchMap(userIDLiveData, new Function<String, LiveData<DataSnapshot>>() {
        @Override
        public LiveData<DataSnapshot> apply(String input) {
            return new FirebaseQueryLiveData(HOME_QUESTIONS_REF.child(input));
        }
    });

    private LiveData<List<AnsweredQuestion>> homeAnsweredQuestions = Transformations.switchMap(homeQuestionsDataSnapShot, new Function<DataSnapshot, LiveData<List<AnsweredQuestion>>>() {
        @Override
        public LiveData<List<AnsweredQuestion>> apply(DataSnapshot input) {
            List<AnsweredQuestion> questions = new ArrayList<>();
            for(DataSnapshot data: input.getChildren()){
                questions.add(data.getValue(AnsweredQuestion.class));
            }
            MutableLiveData<List<AnsweredQuestion>>ret = new MutableLiveData<>();
            ret.setValue(questions);
            return ret;
        }
    });


    public void setUserID(String userID){
        userIDLiveData.setValue(userID);
    }

    public LiveData<List<AnsweredQuestion>> getHomeAnsweredQuestions(){
        return homeAnsweredQuestions;
    }

}
