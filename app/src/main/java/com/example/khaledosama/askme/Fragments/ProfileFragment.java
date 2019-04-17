package com.example.khaledosama.askme.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.Adapters.HomeRecyclerAdapter;
import com.example.khaledosama.askme.Models.ProfileQuestionsViewModel;
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
public class ProfileFragment extends Fragment {

    TextView profileName,profileKinckName,numOfQuestions,numOfFollowers,numOfFollowing;
     public static String currentUserID;
     public static ArrayList<AnsweredQuestion> list;
     public static HomeRecyclerAdapter mHomeRecyclerAdapter;
     public static String ANSWERED_QUESTIONS_REF;
     public static RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.profile_fragment,container,false);
        recyclerView = retView.findViewById(R.id.profileRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileName = retView.findViewById(R.id.profileSliderName);
        profileKinckName = retView.findViewById(R.id.profileSliderKnickName);
        numOfFollowers = retView.findViewById(R.id.numOfFollowers);
        numOfFollowing = retView.findViewById(R.id.numOfFollowing);
        numOfQuestions = retView.findViewById(R.id.numOfQuestions);

        currentUserID = Profile.getCurrentProfile().getId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    int followed = dataSnapshot.child("followers").getValue(Integer.class);
                    int following = dataSnapshot.child("following").getValue(Integer.class);
                    int numOfQuestion = dataSnapshot.child("numOfQuestions").getValue(Integer.class);

                    numOfFollowers.setText(String.valueOf(followed));
                    numOfFollowing.setText(String.valueOf(following));
                    numOfQuestions.setText(String.valueOf(numOfQuestion));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();

            }
        });

        ProfileQuestionsViewModel viewModel= ViewModelProviders.of(this).get(ProfileQuestionsViewModel.class);
        viewModel.setUserID(currentUserID);

        viewModel.getProfileQuestions().observe(this, new Observer<List<AnsweredQuestion>>() {
            @Override
            public void onChanged(@Nullable List<AnsweredQuestion> answeredQuestions) {
                mHomeRecyclerAdapter = new HomeRecyclerAdapter((ArrayList<AnsweredQuestion>) answeredQuestions);
                recyclerView.setAdapter(mHomeRecyclerAdapter);
            }
        });

        return retView;
    }
}
