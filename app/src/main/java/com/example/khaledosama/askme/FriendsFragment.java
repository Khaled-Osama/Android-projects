package com.example.khaledosama.askme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    static User currentUser;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }
    public static FriendsFragment newInstance(User mCurrentUser){
        currentUser = mCurrentUser;
        return new FriendsFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container ,
                             Bundle savedInstance){
        View retView = inflater.inflate(R.layout.friends_fragment,container,false);
        RecyclerView friendsRecyclerView = retView.findViewById(R.id.friendsRecyclerView);
        ArrayList<User> friends = new ArrayList<>();
        FriendsAdapter adapter = new FriendsAdapter(friends,getContext(),currentUser);
        friendsRecyclerView.setAdapter(adapter);
        return retView;
    }


}
