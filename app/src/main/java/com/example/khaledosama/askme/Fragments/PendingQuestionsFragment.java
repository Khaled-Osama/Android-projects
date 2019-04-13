package com.example.khaledosama.askme.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.Models.PendingQuestionsViewModel;
import com.example.khaledosama.askme.NonAnsweredQuestion;
import com.example.khaledosama.askme.Adapters.PendingQuestionAdapter;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingQuestionsFragment extends Fragment {
    public static ArrayList<NonAnsweredQuestion> list;
    public static PendingQuestionAdapter mPendingQuestionAdapter;
    public static String currentUserID;
    public static RecyclerView recyclerView;
    static FragmentManager fm;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
           }
    public static PendingQuestionsFragment newInstance(String user){
        currentUserID = user;
        return new PendingQuestionsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.pending_question_fragment,container,false);

        if(getActivity()!=null){fm=getActivity().getFragmentManager();}
        else {fm = null;}

        recyclerView = retView.findViewById(R.id.pendingQuestionRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference pendingQuestionsRef= FirebaseDatabase.getInstance().getReference().child("pendingQuestionsRef")
                .child(currentUserID);

        PendingQuestionsViewModel viewModel = ViewModelProviders.of(this).get(PendingQuestionsViewModel.class);
        viewModel.setUserID(currentUserID);

        viewModel.getPendingQuestios().observe(this, new Observer<List<NonAnsweredQuestion>>() {
            @Override
            public void onChanged(@Nullable List<NonAnsweredQuestion> nonAnsweredQuestions) {
                mPendingQuestionAdapter = new PendingQuestionAdapter((ArrayList<NonAnsweredQuestion>)nonAnsweredQuestions, fm);
            }
        });

        list = new ArrayList<NonAnsweredQuestion>();
        pendingQuestionsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NonAnsweredQuestion question = dataSnapshot.getValue(NonAnsweredQuestion.class);
                list.add(question);
                mPendingQuestionAdapter.notifyItemInserted(list.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //loadQuestions();
        //mPendingQuestionAdapter = new PendingQuestionAdapter(list,fm,currentUser);
        recyclerView.setAdapter(mPendingQuestionAdapter);

        return retView;
    }

    public static void loadQuestions(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("pendingQuestionsRef").child(currentUserID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    NonAnsweredQuestion question = data.getValue(NonAnsweredQuestion.class);
                    list.add(question);
                }
                showResults();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private static void showResults() {
        //mPendingQuestionAdapter = new PendingQuestionAdapter(list,fm,currentUser);
        recyclerView.setAdapter(mPendingQuestionAdapter);
    }

    public static void deleteItem (final User user, int pos, final String ques, String ans,String date){
        list.remove(pos);
        mPendingQuestionAdapter.notifyDataSetChanged();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("pendingQuestionsRef").child(user.id);
        /*ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    NonAnsweredQuestion question=(NonAnsweredQuestion)data.getValue(NonAnsweredQuestion.class);
                    if(question.getQuestion().equals(ques)){
                        DatabaseReference removeRef = FirebaseDatabase.getInstance().getReference().child("pendingQuestionsRef").
                                child(user.id).child(data.getKey());
                        removeRef.removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        AnsweredQuestion answeredQuestion = new AnsweredQuestion(ques,ans,date);
        HomeFragment.addItem(user,answeredQuestion);
        ProfileFragment.addItem(user,answeredQuestion);
    }
}