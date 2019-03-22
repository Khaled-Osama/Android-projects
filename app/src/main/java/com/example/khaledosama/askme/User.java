package com.example.khaledosama.askme;

import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

public class User implements Serializable {
    public String fName,lName,knickName,id,fullName;
    public int followers,following,numOfQuestions;
    public int userType;
    Map <String,Boolean>friends = new HashMap<>();
    public User(){

    }
    public User(String id,String fName,String lName,int userType,String knickName,
                int followers,int following,int numOfQuestions){
        this.fName=fName;
        this.lName=lName;
        this.userType=userType;
        this.id = id;
        this.followers = followers;
        this.following = following;
        this.numOfQuestions=numOfQuestions;
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(fName); nameBuilder.append(" "); nameBuilder.append(lName);
        fullName = nameBuilder.toString();
        StringBuilder knickNameBuilder = new StringBuilder();
        knickNameBuilder.append("("); knickNameBuilder.append(knickName); knickNameBuilder.append(")");
        this.knickName = knickNameBuilder.toString();
    }
    public void follow(User friend){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference friendsRef = database.getReference().child("friends").child(id);
        String key=friendsRef.push().getKey();
        Map<String,String> map = new HashMap<>();
        map.put("id",friend.id);
        friendsRef.push().setValue(map);
        following++;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user").child(id).child("following");
        ref.setValue(following);

        ref = FirebaseDatabase.getInstance().getReference().child("user").child(friend.id).child("followers");
        friend.followers++;
        ref.setValue(friend.followers);
    }
    public void unfollow(final User friend){
        String key= new String();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("friends").child(id);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            HashMap<String,String> map = (HashMap<String, String>) data.getValue();
                            if(map.get("id").equals(friend.id)){
                                String k = data.getKey();
                                removeKey(k);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        FirebaseDatabase.getInstance().getReference().child("friends").child(id).child(friend.id).removeValue();
        following--;
    }

    private void removeKey(String k){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("friends").child(id).child(k);
        ref.removeValue();
    }

    public boolean follow(final String uID){
        ArrayList<String>list=getFriends();
        boolean ok = false;
        for(String friendID:list){
            if(friendID.equals(uID))ok=true;
        }
        return ok;
    }

    private ArrayList<String> getFriends(){
        final ArrayList<String>friends = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("friends").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    friends.add(dataSnapshot.child("id").getValue(String.class));
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return friends;
    }
private interface FirebaseCallback{
        public void onCallBack(ArrayList<String> list);
    }
}