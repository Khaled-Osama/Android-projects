package com.example.khaledosama.askme.Activities;

import android.app.SearchManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.support.annotation.Nullable;
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
import com.example.khaledosama.askme.Fragments.AskedQuestionsFragment;
import com.example.khaledosama.askme.Fragments.FriendsFragment;
import com.example.khaledosama.askme.Fragments.HomeFragment;
import com.example.khaledosama.askme.Models.UsersViewModel;
import com.example.khaledosama.askme.NonAnsweredQuestion;
import com.example.khaledosama.askme.Fragments.PendingQuestionsFragment;
import com.example.khaledosama.askme.Fragments.ProfileFragment;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.SearchAdapter;
import com.example.khaledosama.askme.User;
import com.facebook.Profile;
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
    UsersViewModel usersViewModel;



    String currentUser;

    AHBottomNavigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_ativity);
        navigation=findViewById(R.id.bottom_navigation);

        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
        currentUser = Profile.getCurrentProfile().getId();


        List<AHBottomNavigationItem>items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getResources().getString(R.string.item1), R.drawable.home));
        items.add(new AHBottomNavigationItem(getResources().getString(R.string.item5), R.drawable.profile));
        items.add(new AHBottomNavigationItem(getResources().getString(R.string.item3), R.drawable.pending_questions));
        items.add(new AHBottomNavigationItem(getResources().getString(R.string.item4), R.drawable.asked_questions));
        items.add(new AHBottomNavigationItem(getResources().getString(R.string.item6), R.drawable.friends));
        navigation.addItems(items);

        // set background color
        navigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));
        navigation.setAccentColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(this, R.color.colorAccent))) );
        navigation.setInactiveColor(Color.parseColor("#747474"));
        navigation.setCurrentItem(0);


        final FragmentManager fm =getSupportFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout,HomeFragment.newInstance());
        transaction.commit();



        navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            Fragment selectedFragment = null;
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(!wasSelected){
                    switch(position){
                        case 0:
                            selectedFragment =HomeFragment.newInstance();

                            break;
                        case 1:
                            selectedFragment = ProfileFragment.newInstance();
                            break;
                        case 2:
                            selectedFragment= PendingQuestionsFragment.newInstance();
                            break;
                        case 3:
                            selectedFragment = AskedQuestionsFragment.newInstance();
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
    ArrayList<String>names;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu=menu;
        getMenuInflater().inflate(R.menu.search_menu,menu);
            manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView)menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            //searchView.setIconifiedByDefault(false);
            searchView.setSubmitButtonEnabled(true);

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
                public boolean onQueryTextChange(final String s) {


                    usersViewModel.getUsers().observe(AccountActivity.this, new Observer<List<User>>() {
                        @Override
                        public void onChanged(@Nullable List<User> users) {
                            loadItems(s, (ArrayList<User>)users);

                        }
                    });
                    return true;
                }
            });

            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionSelect(int i) {
                    return true;
                }

                @Override
                public boolean onSuggestionClick(int i) {
                    String s = names.get(i);
                    Intent intent =new Intent(getApplicationContext(),SearchResultActivity.class);
                    intent.setAction(Intent.ACTION_SEARCH);
                    intent.putExtra(SearchManager.QUERY,s);
                    intent.putExtra("currentUser",currentUser);
                    startActivity(intent);
                    return true;
                }
            });
        return true;
    }
    private void loadItems(String query, ArrayList<User>users){
        ArrayList<String>userNames = getUserNames(query, users);
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

    public ArrayList<String> getUserNames(String query, ArrayList<User> users) {
        names=new ArrayList<>();
        for(User user:users){
            if(user.id.equals(currentUser))continue;
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
