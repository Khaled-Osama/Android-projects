package com.example.khaledosama.askme.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.AskedQuestionsFragment;
import com.example.khaledosama.askme.FriendsFragment;
import com.example.khaledosama.askme.HomeFragment;
import com.example.khaledosama.askme.NonAnsweredQuestion;
import com.example.khaledosama.askme.PendingQuestionsFragment;
import com.example.khaledosama.askme.ProfileFragment;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.SearchAdapter;
import com.example.khaledosama.askme.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AccountActivity extends AppCompatActivity {

    List<AnsweredQuestion>homeQuestions;
    List<NonAnsweredQuestion>pendingQuestions;
    List<User>users;



    User currentUser;

    AHBottomNavigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_ativity);
        navigation=findViewById(R.id.bottom_navigation);
        users = new ArrayList<>();
        DatabaseReference usersref = FirebaseDatabase.getInstance().getReference().child("user");
        usersref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    users.add(data.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        currentUser = (User)getIntent().getSerializableExtra("UserClass");
        Log.v("WWW", currentUser.fName);
        List<AHBottomNavigationItem>items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getResources().getString(R.string.item1), R.drawable.home));
        items.add(new AHBottomNavigationItem(getResources().getString(R.string.item5), R.drawable.profile));
        items.add(new AHBottomNavigationItem(getResources().getString(R.string.item3), R.drawable.pending_questions));
        items.add(new AHBottomNavigationItem(getResources().getString(R.string.item4), R.drawable.asked_questions));
        navigation.addItems(items);

        // set background color
        navigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));
        navigation.setAccentColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(this, R.color.colorAccent))) );
        navigation.setInactiveColor(Color.parseColor("#747474"));
        navigation.setCurrentItem(0);


        final FragmentManager fm =getSupportFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout,HomeFragment.newInstance(currentUser));
        transaction.commit();



        navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            Fragment selectedFragment = null;
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(!wasSelected){
                    switch(position){
                        case 0:
                            selectedFragment =HomeFragment.newInstance(currentUser);

                            break;
                        case 1:
                            selectedFragment = ProfileFragment.newInstance(currentUser);
                            break;
                        case 2:
                            selectedFragment= PendingQuestionsFragment.newInstance(currentUser);
                            break;
                        case 3:
                            selectedFragment = AskedQuestionsFragment.newInstance(currentUser);
                            break;
                        case 4:
                            selectedFragment = FriendsFragment.newInstance(currentUser);

                        default:
                            break;
                    }
                    fm.beginTransaction().replace(R.id.frame_layout,selectedFragment).commit();

                }
                return true;
            }
        });



    }
    Menu menu;
    SearchManager manager;
    SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu=menu;
        getMenuInflater().inflate(R.menu.search_menu,menu);
            manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);searchView = (SearchView)menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Intent intent =new Intent(getApplicationContext(),SearchResultActivity.class);
                    intent.setAction(Intent.ACTION_SEARCH);
                    intent.putExtra(SearchManager.QUERY,s);
                    intent.putExtra("currentUser",currentUser);
                    startActivity(intent);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    loadItems(s);
                    return true;
                }
            });
        return true;
    }
    private void loadItems(String query){
        ArrayList<String>userNames = getUserNames(query);
        String[] columns = new String[] {"_id","name"};
        Object[] item = new Object[] {0,"none"};
        MatrixCursor cursor =new MatrixCursor(columns);
        for(int i=0;i<userNames.size();i++){
            item[0]=i;
            item[1]=userNames.get(i);
                cursor.addRow(item);
        }


        searchView.setSuggestionsAdapter(new SearchAdapter(userNames, cursor, this));
    }

    public ArrayList<String> getUserNames(String query) {
        ArrayList<String>names=new ArrayList<>();
        for(User user:users){
            if(user.id.equals(currentUser.id))continue;
            StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append(user.fName);
            nameBuilder.append(" ");
            nameBuilder.append(user.lName);
            String name=nameBuilder.toString();
            if(contain(name,query)) {
                names.add(nameBuilder.toString());
            }
        }

        return names;
    }
    private boolean contain(String parent,String child){
        parent = parent.toLowerCase(); child = child.toLowerCase();
        for(int i=0;i<parent.length()&&i+child.length()<=parent.length();i++){
            if(parent.substring(i,i+child.length()).equals(child))return true;
        }
        return false;

    }
}
