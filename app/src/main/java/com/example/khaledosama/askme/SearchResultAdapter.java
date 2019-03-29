package com.example.khaledosama.askme;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.khaledosama.askme.Activities.FriendProfile;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Button state;
        public CircleImageView image;
        View mView;
        public ViewHolder(View view){
            super(view);
            mView=view;
            name = view.findViewById(R.id.searchResultName);
            state = view.findViewById(R.id.searchResultfollowButton);
            image = view.findViewById(R.id.searchResultImage);
        }
    }
    private List<User> searchResultUsers;
    private Context context;
    private User currentUser;
    public SearchResultAdapter(ArrayList<User>searchUsers, Context context,User currentUser){
        searchResultUsers = searchUsers;
        this.context = context;
        this.currentUser = currentUser;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        final User user = searchResultUsers.get(position);
        holder.name.setText(user.fullName);
        final Button stateButton = holder.state;
        if(currentUser.follow(user.id))
            stateButton.setText("Unfollow");
        else
            stateButton.setText("Follow");


         stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stateButton.getText().equals("Follow")){
                    currentUser.follow(user);
                    stateButton.setText("Unfollow");
                }
                else {
                    currentUser.unfollow(user);
                    stateButton.setText("Follow");
                }
            }
        });
         holder.mView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 User user1 = searchResultUsers.get(position);
                 Intent intent = new Intent(context,FriendProfile.class);
                 intent.putExtra("currentUser",currentUser);
                 intent.putExtra("friendProfile",user1);
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 context.startActivity(intent);
             }
         });



    }

    @Override
    public int getItemCount() {
        return searchResultUsers.size();
    }


}
