package com.example.khaledosama.askme;

public class NonAnsweredQuestion {
    private String question ;
    private String date;
    private String askedUserID;
    private String questionID;

    public NonAnsweredQuestion(){}

    public NonAnsweredQuestion(String mQuestion,String mDate,String userIID, String mQuestionID){
        question = mQuestion;
        date = mDate;
        askedUserID = userIID;
        questionID = mQuestionID;
    }
    public String getQuestion(){
        return question;
    }
    public String getDate(){
        return date;
    }
    public String getAskedUserID(){return askedUserID;}
    public String getQuestionID(){return questionID;}

    /*public void setQuestion(String question) {
        this.question = question;
    }

    public void setAskedUserID(String askedUserID) {
        this.askedUserID = askedUserID;
    }

    public void setDate(String date) {
        this.date = date;
    }*/
}
