package com.example.khaledosama.askme;

import java.net.URL;

public class AnsweredQuestion {
    String question;
    String answer;
    String date;
    URL profilePic;

    public AnsweredQuestion(){}

    public AnsweredQuestion (String mQuestion, String mAnswer,String date){
        this.question = mQuestion;
        this.answer = mAnswer;
        this.date = date;
    }
    public AnsweredQuestion(String mQuestion, String mAnswer,URL mProfilePic){
        this.question = mQuestion;
        this.answer = mAnswer;
        this.profilePic = mProfilePic;
    }

    public String getQuestion(){
        return question;
    }
    public String getAnswer(){
        return answer;
    }
    public String getDate(){return date;}
}
