package com.example.khaledosama.askme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class answerFragmentDialog extends DialogFragment {
    int position;
    String question,askedUserID;
    User currentUser;
    public answerFragmentDialog(){super();}
    private void addQuestionToFollowersHome(final AnsweredQuestion question){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("friends").child(currentUser.id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String followerID = (String)data.child("id").getValue();
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("homeAnsweredQuestions").
                            child(followerID);
                    ref2.push().setValue(question);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.answer_dialog,null);
        builder.setView(v);
        final Bundle bundle = getArguments();
        position = bundle.getInt("position");
        question = bundle.getString("question");
        askedUserID = bundle.getString("askedUserID");
        currentUser=(User)bundle.getSerializable("currentUser");


        final EditText editText = v.findViewById(R.id.dialogAnswer);
        final ImageButton submit = v.findViewById(R.id.dialogSubmitButton);
        submit.setEnabled(false);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()!=0){
                    submit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat("h:mm a");
                Date currentTime = Calendar.getInstance().getTime();
                String date = df.format(currentTime);
                String answer = editText.getText().toString();
                PendingQuestionsFragment.deleteItem(currentUser, position,question,answer,date);

                AnsweredQuestion answeredQuestion = new AnsweredQuestion(question,answer,date);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("askedQuestionsRef").child(askedUserID);
                ref.push().setValue(answeredQuestion);

                ref = FirebaseDatabase.getInstance().getReference().child("user").child(currentUser.id).child("numOfQuestions");
                currentUser.numOfQuestions++;
                ref.setValue(currentUser.numOfQuestions);

                addQuestionToFollowersHome(answeredQuestion);

                dismiss();
            }
        });

        //builder.setCustomTitle(t);
        return builder.create();

    }

}
