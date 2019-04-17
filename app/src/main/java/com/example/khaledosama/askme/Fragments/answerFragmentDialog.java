package com.example.khaledosama.askme.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.khaledosama.askme.AnsweredQuestion;
import com.example.khaledosama.askme.Fragments.PendingQuestionsFragment;
import com.example.khaledosama.askme.R;
import com.example.khaledosama.askme.User;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class answerFragmentDialog extends DialogFragment {
    String question,askedUserID, userID, questionID;
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

        question = bundle.getString("question");
        askedUserID = bundle.getString("askedUserID");
        questionID = bundle.getString("questionID");
        Log.v("WWW", questionID);


        userID = Profile.getCurrentProfile().getId();


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
                //PendingQuestionsFragment.deleteItem(currentUser, position,question,answer,date);


                final AnsweredQuestion answeredQuestion = new AnsweredQuestion(question,answer,date);

                //Push to askedQuestions as answered question.
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(getString(R.string.ASKED_QUESTIONS_REF)).child(askedUserID);
                ref.push().setValue(answeredQuestion);
                // Push to Profile questions.
                ref = FirebaseDatabase.getInstance().getReference().
                        child(getString(R.string.PROFILE_ANSWERED_QUESTIONS)).
                        child(userID);

                ref.push().setValue(answeredQuestion);

                // remove from pendingQuestions

                ref = FirebaseDatabase.getInstance().getReference().
                        child(getString(R.string.PENDING_QUESTIONS_REF)).
                        child(userID).child(questionID);

                ref.removeValue();

                // increment numOfQuestios

                final DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference("user").child(userID).child("numOfQuestions");
                questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long numOfQuestions = dataSnapshot.getValue(Long.class);
                        questionsRef.setValue(numOfQuestions + 1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                ref = FirebaseDatabase.getInstance().getReference().child(getString(R.string.FRIENDS_REF)).child(userID);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            String id = dataSnapshot1.getValue(String.class);

                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().
                                    child(getString(R.string.HOME_QUESTIONS)).child(id);

                            ref2.push().setValue(answeredQuestion);
                        }
                        dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });

        return builder.create();

    }

}
