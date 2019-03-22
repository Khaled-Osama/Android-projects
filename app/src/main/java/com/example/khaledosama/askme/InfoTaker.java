package com.example.khaledosama.askme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfoTaker extends AppCompatActivity {
    CircleButton circleButton;
    EditText phone,knickName,fName,lName;
    CircleImageView profilePic;
    String userID;
    int userType;
    FirebaseDatabase database;
    DatabaseReference userRefrence ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_taker);



        database = FirebaseDatabase.getInstance();
        userRefrence = database.getReference();
        circleButton = findViewById(R.id.circleButton);
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        knickName=findViewById(R.id.knickName);
        profilePic = findViewById(R.id.profilePic);

        userID = AccessToken.getCurrentAccessToken().getUserId();
        userType = 1;

        fillEditTexts();

        /*AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                PhoneNumber phoneNumber = account.getPhoneNumber();
                userID = account.getId();
                if(phoneNumber!=null){
                    phone.setText(phoneNumber.toString());
                    userType=1;
                }
                else{
                    phone.setText(account.getEmail());
                    userType=2;
                }
            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });*/






        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ok()) {
                    Intent intent = new Intent(InfoTaker.this, AccountActivity.class);
                    User user= new User(userID,
                            fName.getText().toString(),lName.getText().toString(),
                            userType,
                            knickName.getText().toString(),0,0,0
                    );
                    userRefrence.child("user").child(userID).setValue(user);
                    intent.putExtra("userClass",user);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private boolean ok(){
        if(fName.getText().toString().trim().equals("")||lName.getText().toString().trim().equals("")){
            Toast.makeText(this, "please write your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void fillEditTexts(){

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse response){
                try {
                    fName.setText(jsonObject.getString("first_name"));
                    lName.setText(jsonObject.getString("last_name"));
                    String image_url = "https://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=normal";
                    Picasso.with(InfoTaker.this).load(image_url).into(profilePic);
                }



                catch(JSONException e){

                }

            }

        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }

}
