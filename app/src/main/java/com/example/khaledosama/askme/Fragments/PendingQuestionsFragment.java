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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.Models.PendingQuestionsViewModel;
import com.example.khaledosama.askme.NonAnsweredQuestion;
import com.example.khaledosama.askme.Adapters.PendingQuestionAdapter;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.User;
import com.facebook.Profile;
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
    public static PendingQuestionsFragment newInstance(){
        return new PendingQuestionsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.pending_question_fragment,container,false);

        if(getActivity()!=null){fm=getActivity().getFragmentManager();}
        else {fm = null;}

        currentUserID = Profile.getCurrentProfile().getId();

        recyclerView = retView.findViewById(R.id.pendingQuestionRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference pendingQuestionsRef= FirebaseDatabase.getInstance().getReference().child(getString(R.string.PENDING_QUESTIONS_REF))
                .child(currentUserID);

        PendingQuestionsViewModel viewModel = ViewModelProviders.of(this).get(PendingQuestionsViewModel.class);
        viewModel.setUserID(currentUserID);

        viewModel.getPendingQuestios().observe(this, new Observer<List<NonAnsweredQuestion>>() {
            @Override
            public void onChanged(@Nullable List<NonAnsweredQuestion> nonAnsweredQuestions) {
                mPendingQuestionAdapter = new PendingQuestionAdapter((ArrayList<NonAnsweredQuestion>)nonAnsweredQuestions, fm);
                recyclerView.setAdapter(mPendingQuestionAdapter);
            }
        });

        return retView;
    }

}