package com.example.khaledosama.askme.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.khaledosama.askme.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    final int APP_REQUEST_CODE = 1;
    FirebaseDatabase database;
    DatabaseReference ref;
    String userID;
    CallbackManager callbackManager;
    LoginButton loginButton;
    AccessToken accessToken;
    ChildEventListener childEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String tmp = dataSnapshot.getKey();
            if(tmp.equals(userID)){
                launchAccountActivity();
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("user");

        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        if(!loggedOut){
            launchAccountActivity();
        }


        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                userID = accessToken.getUserId();
                ref.addChildEventListener(childEventListener);


                /*Intent intent = new Intent(getApplicationContext(), InfoTaker.class);
                startActivity(intent);*/

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        /*database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("user");
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                userID = account.getId();
            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });
        ref.addChildEventListener(childEventListener);*/
    }
    @Override
    protected void onPause(){
        super.onPause();
        //ref.removeEventListener(childEventListener);
    }

    private void launchAccountActivity() {
        Intent intent = new Intent(this,AccountActivity.class);
        startActivity(intent);
        finish();
    }
    private void launchInfoTakerActivity(){
        Intent intent = new Intent(this,InfoTakerActivity.class);
        intent.putExtra("UserAccessToken", accessToken);
        startActivity(intent);
        finish();
    }

    protected void onActivityResult(final int requestCode,final int resultCode,final Intent data){

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*private void onLogin(final LoginType loginType){
        final Intent intent = new Intent(this,AccountKitActivity.class);


        final AccountKitConfiguration configuration = new AccountKitConfiguration.AccountKitConfigurationBuilder(
                loginType,
                AccountKitActivity.ResponseType.TOKEN
        ).build();
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,configuration);
        startActivityForResult(intent,APP_REQUEST_CODE);
    }

    public void onPhoneLogin(View view) {
        onLogin(LoginType.PHONE);

    }

    public void onEmailLogin(View view) {
        onLogin(LoginType.EMAIL);
    }*/

}
