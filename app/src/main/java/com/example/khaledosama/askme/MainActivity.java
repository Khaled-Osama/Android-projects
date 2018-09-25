package com.example.khaledosama.askme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    final int APP_REQUEST_CODE = 1;
    FirebaseDatabase database;
    DatabaseReference ref;
    String userID;
    ChildEventListener childEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String tmp = dataSnapshot.getKey();
            if(tmp.equals(userID)){
                launchAccountActivity(dataSnapshot.getValue(User.class));
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
        database = FirebaseDatabase.getInstance();
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
        ref.addChildEventListener(childEventListener);

        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onPause(){
        super.onPause();
        ref.removeEventListener(childEventListener);
    }

    private void launchAccountActivity(User userClass) {
        Intent intent = new Intent(this,AccountActivity.class);
        intent.putExtra("UserClass",userClass);
        startActivity(intent);
        finish();
    }
    private void launchInfoTakerActivity(){
        Intent intent = new Intent(this,InfoTaker.class);
        startActivity(intent);
        finish();
    }

    protected void onActivityResult(final int requestCode,final int resultCode,final Intent data){
        if(requestCode == APP_REQUEST_CODE){
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if(loginResult.getError()!=null){
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();

            }
            else if(loginResult.getAccessToken()!=null){
                launchInfoTakerActivity();
            }

        }



    }

    private void onLogin(final LoginType loginType){
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
    }

}
