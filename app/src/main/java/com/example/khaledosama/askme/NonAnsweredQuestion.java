package com.example.khaledosama.askme;

public class NonAnsweredQuestion {
    private String question ;
    private String date;
    private String askedUserID;

    public NonAnsweredQuestion(){}

    public NonAnsweredQuestion(String mQuestion,String mDate,String userIID){
        question = mQuestion;
        date = mDate;
        this.askedUserID = userIID;
    }
    public String getQuestion(){
        return question;
    }
    public String getDate(){
        return date;
    }
    public String getAskedUserID(){return askedUserID;}

}
