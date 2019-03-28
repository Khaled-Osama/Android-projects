package com.example.khaledosama.askme.Activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.SearchResultAdapter;
import com.example.khaledosama.askme.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class SearchResultActivity extends AppCompatActivity {
    static RecyclerView recyclerView;
    static ArrayList<User> usersSearchResults;
    public static User currentuser;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        usersSearchResults = new ArrayList<>();
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            context = getApplicationContext();

            final String query = intent.getStringExtra(SearchManager.QUERY);
            recyclerView = findViewById(R.id.searchResultRecyclerView);
            currentuser = (User)intent.getSerializableExtra("currentUser");
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("user");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        User user = data.getValue(User.class);
                        String name = user.fullName;
                        if(name.trim().equals(query.trim()))
                            usersSearchResults.add(user);
                        showList();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    }
    private void showList(){
        SearchResultAdapter adapter = new SearchResultAdapter(usersSearchResults,context,currentuser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


}
