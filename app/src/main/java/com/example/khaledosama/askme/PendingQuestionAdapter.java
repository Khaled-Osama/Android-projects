package com.example.khaledosama.askme;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;

public class PendingQuestionAdapter extends RecyclerView.Adapter<PendingQuestionAdapter.MyViewHolder> {


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View view;
        public TextView mQuestion,mDate;
        public ImageButton dialogButton;
        public MyViewHolder(View mView){
            super(mView);
            view = mView;
            mQuestion= mView.findViewById(R.id.nonAnsweredQuestion);
            mDate = mView.findViewById(R.id.nonAsnweredQuestionDate);
            dialogButton = mView.findViewById(R.id.openDialogButton);

        }


    }
    public static FragmentManager fm;
    public User currentUser;
    private ArrayList<NonAnsweredQuestion>questions;
    public PendingQuestionAdapter(ArrayList<NonAnsweredQuestion>mQuestions, FragmentManager fm,User user)
    {
        questions=mQuestions;
        this.fm = fm;
        currentUser = user;
    }

    @NonNull
    @Override
    public PendingQuestionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.non_answered_question,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingQuestionAdapter.MyViewHolder holder, final int position) {
        final NonAnsweredQuestion question = questions.get(position);
        holder.mQuestion.setText(question.getQuestion());
        holder.mDate.setText(question.getDate());
        holder.dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerFragmentDialog dialog = new answerFragmentDialog();
                Bundle bundle = new Bundle();
                bundle.putString("question",question.getQuestion());
                bundle.putInt("position",position);
                bundle.putString("askedUserID",question.getAskedUserID());
                bundle.putSerializable("currentUser",currentUser);
                dialog.setArguments(bundle);
                dialog.show( fm,"answer question");
            }
        });

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
