package com.example.khaledosama.askme.Activities;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.khaledosama.askme.Models.UsersViewModel;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.Adapters.SearchResultAdapter;
import com.example.khaledosama.askme.User;
import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    static RecyclerView recyclerView;
    static ArrayList<User> usersSearchResults;
    public static Context context;
    String userID;
    UsersViewModel usersViewModel;
    SearchResultAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        usersSearchResults = new ArrayList<>();
        Intent intent = getIntent();
        userID = Profile.getCurrentProfile().getId();

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            context = getApplicationContext();

            final String query = intent.getStringExtra(SearchManager.QUERY);
            recyclerView = findViewById(R.id.searchResultRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            usersViewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
            usersViewModel.getUsers().observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    List<User> matchedUsers = new ArrayList<>();
                    for(User user:users){
                        if(user.fullName.trim().substring(0, Math.min(query.length(), user.fullName.length())).toLowerCase().equals(query.trim().toLowerCase()))
                            matchedUsers.add(user);

                    }
                    adapter = new SearchResultAdapter((ArrayList<User>)matchedUsers, SearchResultActivity.this, userID);
                    recyclerView.setAdapter(adapter);
                }
            });


        }


    }

}
