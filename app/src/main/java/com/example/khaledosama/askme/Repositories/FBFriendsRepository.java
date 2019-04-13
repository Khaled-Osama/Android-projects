package com.example.khaledosama.askme.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.khaledosama.askme.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FBFriendsRepository extends ViewModel {

    public ArrayList<User> getFBFriends(String userID){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.v("WWW",object.toString());
                    JSONArray jsonArray = object.getJSONObject("friends").getJSONArray("data");
                    Log.v("WWW",object.toString());
                    for (int i = 0; i<jsonArray.length();i++){
                        Log.v("WWW", jsonArray.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "friends");
        request.setParameters(parameters);
        request.executeAsync();
        return null;
    }

}
