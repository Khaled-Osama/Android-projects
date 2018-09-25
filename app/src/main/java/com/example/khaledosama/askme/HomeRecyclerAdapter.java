package com.example.khaledosama.askme;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder>{

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTV,answerTV,date;
        public MyViewHolder(View mView) {
            super(mView);
            questionTV = mView.findViewById(R.id.homeQuestion);
            answerTV = mView.findViewById(R.id.homeAnswer);
            date = mView.findViewById(R.id.questionDate);
        }

    }

    private ArrayList<AnsweredQuestion> questions;
    public HomeRecyclerAdapter(ArrayList mQuestions){
        questions=mQuestions;
    }


    @NonNull
    @Override
    public HomeRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answered_question,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AnsweredQuestion question = questions.get(position);
        holder.questionTV.setText(question.getQuestion());
        holder.answerTV.setText(question.getAnswer());
        holder.date.setText(question.getDate());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

}
