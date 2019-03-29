package com.example.khaledosama.askme.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khaledosama.askme.Activities.FriendProfile;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.User;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {

    private Context context;
    ArrayList<User> friends;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        ImageView friendImage;
        public MyViewHolder(View mView){
            super(mView);
            name = mView.findViewById(R.id.friendName);
            friendImage = mView.findViewById(R.id.friendImage);

        }
    }
    public FriendsAdapter(ArrayList<User> mFriends,Context mContext){
        friends = mFriends;
        context=mContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final User user = friends.get(position);
        StringBuffer name = new StringBuffer(user.fName+" "+user.lName);
        holder.name.setText(name);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,FriendProfile.class);
                intent.putExtra("friendProfile",user);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void swapList(ArrayList<User> firendUsers){
        friends = firendUsers;
        this.notifyDataSetChanged();
    }

}
