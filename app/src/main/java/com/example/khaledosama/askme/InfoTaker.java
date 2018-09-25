package com.example.khaledosama.askme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.markushi.ui.CircleButton;

public class InfoTaker extends AppCompatActivity {
    CircleButton circleButton;
    EditText phone,knickName,fName,lName;
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
        phone = findViewById(R.id.phoneNumber);
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        knickName=findViewById(R.id.knickName);

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
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
        });


        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ok()) {
                    Intent intent = new Intent(InfoTaker.this, AccountActivity.class);
                    User user= new User(userID,
                            fName.getText().toString(),lName.getText().toString(),
                            phone.getText().toString(),userType,
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
}
