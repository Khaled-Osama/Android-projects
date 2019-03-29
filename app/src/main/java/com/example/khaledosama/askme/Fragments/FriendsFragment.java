package com.example.khaledosama.askme.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khaledosama.askme.Adapters.FriendsAdapter;
import com.example.khaledosama.askme.Models.FriendsViewModel;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.Repositories.FBFriendsRepository;
import com.example.khaledosama.askme.User;
import com.facebook.Profile;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    static String mUserID;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }
    public static FriendsFragment newInstance(String userID){
        mUserID = userID;
        return new FriendsFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container ,
                             Bundle savedInstance){
        View retView = inflater.inflate(R.layout.friends_fragment,container,false);
        RecyclerView friendsRecyclerView = retView.findViewById(R.id.friendsRecyclerView);
        final FriendsAdapter adapter = new FriendsAdapter(null,getContext());
        /*ArrayList<User> friends = new ArrayList<>();
        FriendsAdapter adapter = new FriendsAdapter(friends,getContext(),currentUser);
        friendsRecyclerView.setAdapter(adapter);*/
        /*FriendsViewModel fbViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        fbViewModel.setUserID(mUserID);

        fbViewModel.getFriends().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(@Nullable ArrayList<User> fbFriends) {

                adapter.swapList(fbFriends);

            }
        });*/

        FBFriendsRepository fbFriendsRepository = new FBFriendsRepository();
        ArrayList<User> a = fbFriendsRepository.getFBFriends(Profile.getCurrentProfile().getId());

        return retView;
    }


}
