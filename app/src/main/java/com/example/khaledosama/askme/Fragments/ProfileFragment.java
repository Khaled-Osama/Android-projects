package com.example.khaledosama.askme.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.Adapters.HomeRecyclerAdapter;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView profileName,profileKinckName,numOfQuestions,numOfFollowers,numOfFollowing;
     public static String mUser;
     public static ArrayList<AnsweredQuestion> list;
     public static HomeRecyclerAdapter mHomeRecyclerAdapter;
     public static String ANSWERED_QUESTIONS_REF;
     public static RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    public static ProfileFragment newInstance(String user) {
        mUser = user;
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user").child(mUser);
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

        //profileName.setText(mUser.fullName);
        //profileKinckName.setText(mUser.knickName);


        list = new ArrayList<AnsweredQuestion>();
        DatabaseReference ref1 =FirebaseDatabase.getInstance().getReference().child("profileAnsweredQuestion").child(mUser);
        mHomeRecyclerAdapter = new HomeRecyclerAdapter(list);
        ref1.addChildEventListener(new ChildEventListener() {
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
        //loadQuestions();
        recyclerView.setAdapter(mHomeRecyclerAdapter);
        return retView;
    }
    public static void addItem(User user,AnsweredQuestion question){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("profileAnsweredQuestion").child(user.id);
        ref.push().setValue(question);
    }
    public static void showList(){
        HomeRecyclerAdapter mHomeRecyclerAdapter = new HomeRecyclerAdapter(list);
        recyclerView.setAdapter(mHomeRecyclerAdapter);
    }
}
