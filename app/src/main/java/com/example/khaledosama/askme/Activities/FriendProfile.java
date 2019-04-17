package com.example.khaledosama.askme.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.khaledosama.askme.Adapters.HomeRecyclerAdapter;
import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.Models.ProfileQuestionsViewModel;
import com.example.khaledosama.askme.NonAnsweredQuestion;
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

public class FriendProfile extends AppCompatActivity {
    User user,currentUser;
    String currUserID;
    Button followBtn;
    TextView friendKnickName,friendName,numOfQuestions,numOfFollowers,numOfFollowing;
    RecyclerView recyclerView;
    HomeRecyclerAdapter adapter;
    ArrayList<AnsweredQuestion>questions;
    EditText editText;
    Button askBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        user = (User)getIntent().getSerializableExtra("friendProfile");
        //currentUser = (User)getIntent().getSerializableExtra("currentUser");
        followBtn = findViewById(R.id.friendFollowBtn);
        friendKnickName = findViewById(R.id.friendProfileSliderKnickName);
        friendName = findViewById(R.id.friendProfileSliderName);
        recyclerView = findViewById(R.id.friendProfileQuestionsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editText = findViewById(R.id.questionEditText);
        askBtn = findViewById(R.id.questionSendBtn);
        numOfQuestions = findViewById(R.id.friendNumOfQuestions);
        numOfFollowers = findViewById(R.id.friendNumOfFollowers);
        numOfFollowing = findViewById(R.id.friendNumOfFollowing);

        currUserID = Profile.getCurrentProfile().getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user").child(user.id);
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

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence==""){
                    askBtn.setEnabled(false);
                }
                else{
                    askBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        askBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().trim().equals("")){
                    String str = editText.getText().toString();
                    editText.setText("");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.PENDING_QUESTIONS_REF)).child(user.id);
                    String questionID = ref.push().getKey();
                    NonAnsweredQuestion question = new NonAnsweredQuestion(str,"1:25",currUserID, questionID);
                    ref.child(questionID).setValue(question);
                }
            }
        });

        ProfileQuestionsViewModel viewModel = ViewModelProviders.of(this).get(ProfileQuestionsViewModel.class);
        viewModel.setUserID(user.id);

        viewModel.getProfileQuestions().observe(this, new Observer<List<AnsweredQuestion>>() {
            @Override
            public void onChanged(@Nullable List<AnsweredQuestion> answeredQuestions) {
                adapter = new HomeRecyclerAdapter((ArrayList<AnsweredQuestion>) answeredQuestions);
                recyclerView.setAdapter(adapter);
            }
        });

        friendKnickName.setText(user.knickName);
        friendName.setText(user.fullName);


        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(followBtn.getText().equals("follow")){
                    currentUser.follow(user);
                    followBtn.setText("Unfollow");
                }
                else{
                    currentUser.unfollow(user);
                    followBtn.setText("Follow");
                }
            }
        });
    }


}
