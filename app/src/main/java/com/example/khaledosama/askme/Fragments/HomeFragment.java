package com.example.khaledosama.askme.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.Adapters.HomeRecyclerAdapter;
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
public class HomeFragment extends Fragment {
    public static ArrayList<AnsweredQuestion> list;
    public static HomeRecyclerAdapter mHomeRecyclerAdapter;
    public static String currentUser;

    @Override
   public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);

   }
   public static HomeFragment newInstance(String user){
        currentUser = user;
        return new HomeFragment();
   }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.home_fragment,container,false);
        final PullRefreshLayout pullRefreshLayout = retView.findViewById(R.id.swipeRefreshLayout);

        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.setRefreshing(false);
            }

        });

        pullRefreshLayout.setColor(getResources().getColor(R.color.colorAccent));

        RecyclerView recyclerView = retView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //Log.v("WWW",currentUser.id);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("homeAnsweredQuestions").child("445567519287243");
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

        list = new ArrayList<AnsweredQuestion>();

        mHomeRecyclerAdapter = new HomeRecyclerAdapter(list);

        recyclerView.setAdapter(mHomeRecyclerAdapter);


        return retView;
    }
    public static void addItem(User user,AnsweredQuestion answeredQuestion){
        list.add(answeredQuestion);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("homeAnsweredQuestions").child(user.id);
        ref.push().setValue(answeredQuestion);

        mHomeRecyclerAdapter.notifyDataSetChanged();
    }

}
