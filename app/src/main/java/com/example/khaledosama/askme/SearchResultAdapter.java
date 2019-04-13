package com.example.khaledosama.askme;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.khaledosama.askme.Activities.FriendProfile;
import com.example.khaledosama.askme.Activities.SearchResultActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Callable;

import javax.security.auth.Subject;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

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
    private String userID;
    public SearchResultAdapter(ArrayList<User>searchUsers, Context context,String currentUser){
        searchResultUsers = searchUsers;
        this.context = context;
        this.userID = currentUser;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void  onBindViewHolder(@NonNull final ViewHolder holder,final int position) {

        final User user = searchResultUsers.get(position);
        holder.name.setText(user.fullName);
        final Button stateButton = holder.state;

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("friends").child(userID);

        final PublishSubject<String> subject = PublishSubject.create();
        subject.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                holder.state.setText(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user.id)){
                    subject.onNext("Follow");
                }
                else{
                    subject.onNext("UnFollow");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



         stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().
                        child("friends").child(userID).child(user.id);

                final DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("user").
                        child(userID).child("following");

                final DatabaseReference followerRef = FirebaseDatabase.getInstance().getReference().child("user").
                        child(user.id).child("followers");


                if(stateButton.getText().equals("Follow")){



                    update_increment(followerRef);
                    update_increment(followingRef);
                    Log.v("WWWWWWWWWWWWW", "QQQQQQQ");
                    friendsRef.setValue(user.id);

                    subject.onNext("Unfollow");

                }
                else {
                    friendsRef.removeValue();
                    update_decrement(followingRef);
                    update_decrement(followerRef);
                    subject.onNext("Follow");
                }
            }
        });
         holder.mView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 User user1 = searchResultUsers.get(position);
                 Intent intent = new Intent(context,FriendProfile.class);
                 //intent.putExtra("currentUser",currentUser);
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

    private void update_increment(final DatabaseReference ref){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long current = (long)dataSnapshot.getValue();
                ref.setValue(current + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void update_decrement(final DatabaseReference ref){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long curr = (long) dataSnapshot.getValue();
                ref.setValue(curr - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
