package com.example.khaledosama.askme.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khaledosama.askme.Adapters.HomeRecyclerAdapter;
import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AskedQuestionsFragment extends Fragment {

    static String currentUser;
    ArrayList<AnsweredQuestion> list;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public static AskedQuestionsFragment newInstance(String user){
        currentUser = user;
        return new AskedQuestionsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.asked_questions_fragment,container,false);
        RecyclerView recyclerView = retView.findViewById(R.id.askedQuestionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        final HomeRecyclerAdapter mHomeRecyclerAdapter = new HomeRecyclerAdapter(list);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("askedQuestionsRef").child(currentUser);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AnsweredQuestion question = dataSnapshot.getValue(AnsweredQuestion.class);
                list.add(question);
                mHomeRecyclerAdapter.notifyItemInserted(list.size()-1);
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
        recyclerView.setAdapter(mHomeRecyclerAdapter);
        return retView;
    }

}
