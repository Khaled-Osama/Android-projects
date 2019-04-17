package com.example.khaledosama.askme.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.Adapters.HomeRecyclerAdapter;
import com.example.khaledosama.askme.Models.AskedQuestionsViewModel;
import com.example.khaledosama.askme.Models.HomeQuestionsViewModel;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.User;
import com.facebook.Profile;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public static HomeRecyclerAdapter mHomeRecyclerAdapter;
    public static String currentUserID;
    RecyclerView recyclerView;

    @Override
   public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);

   }
   public static HomeFragment newInstance(){
        return new HomeFragment();
   }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View retView = inflater.inflate(R.layout.home_fragment,container,false);
        final PullRefreshLayout pullRefreshLayout = retView.findViewById(R.id.swipeRefreshLayout);

        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.setRefreshing(false);
            }

        });

        pullRefreshLayout.setColor(getResources().getColor(R.color.colorAccent));

        recyclerView = retView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        currentUserID = Profile.getCurrentProfile().getId();

        HomeQuestionsViewModel viewModel = ViewModelProviders.of(this).get(HomeQuestionsViewModel.class);
        viewModel.setUserID(currentUserID);

        viewModel.getHomeAnsweredQuestions().observe(this, new Observer<List<AnsweredQuestion>>() {
            @Override
            public void onChanged(@Nullable List<AnsweredQuestion> answeredQuestions) {
                mHomeRecyclerAdapter = new HomeRecyclerAdapter((ArrayList<AnsweredQuestion>)answeredQuestions);
                recyclerView.setAdapter(mHomeRecyclerAdapter);
            }
        });


        return retView;
    }

}
