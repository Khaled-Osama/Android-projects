package com.example.khaledosama.askme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfoTakerActivity extends AppCompatActivity {

    CircleImageView imageView;
    EditText fName, lName, knickName;
    CircleButton button;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_taker);
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        knickName = findViewById(R.id.knickName);
        imageView = findViewById(R.id.profilePic);
        button = findViewById(R.id.circleButton);

        profile = Profile.getCurrentProfile();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dataComplete()){

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();
                    DatabaseReference newRef = ref.child("user").child(profile.getId());
                    newRef.setValue(new User(
                            profile.getId(),
                            fName.getText().toString(),
                           lName.getText().toString(),
                            1,
                            knickName.getText().toString(),
                            0,0,0
                    ));
                    Intent intent = new Intent(InfoTakerActivity.this, AccountActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(InfoTakerActivity.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {

                    fName.setText(profile.getFirstName());
                    lName.setText(profile.getLastName());
                    String id = object.getString("id");
                    Picasso.with(InfoTakerActivity.this).load("https://graph.facebook.com/" + id + "/picture?type=large").into(imageView);

                } catch (JSONException e) {
                    Log.v("Error","error while loading data from facebook graph API");
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private boolean dataComplete(){
        if(fName.getText().toString().trim().equals("")||lName.getText().toString().trim().equals("")){
            return false;
        }
        return true;
    }
}