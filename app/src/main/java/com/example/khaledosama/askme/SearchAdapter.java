package com.example.khaledosama.askme;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.khaledosama.askme.R;

import java.util.List;

public class SearchAdapter extends CursorAdapter {
    List<String>userNames;
    TextView textView;

    public SearchAdapter(List names,Cursor cursor,Context context){
        super(context,cursor,false);
        userNames=names;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.friend_item,viewGroup,false);
        textView = view.findViewById(R.id.friendName);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.v("WWW",userNames.get(0));
        textView.setText(userNames.get(cursor.getPosition()));
    }
}