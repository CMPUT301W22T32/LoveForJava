package com.example.loveforjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList2 extends ArrayAdapter<String> {
    private ArrayList<String> userNames;
    private ArrayList<String> commentsBody;
    private Context context;

    public CustomList2(Context context, ArrayList<String> userNames, ArrayList<String> commentsBody) {
        super(context, 0, userNames);
        this.userNames = userNames;
        this.commentsBody = commentsBody;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_of_comments, parent, false);
        }

        String name = userNames.get(position);
        String body = commentsBody.get(position);

        TextView personText = view.findViewById(R.id.personName);
        TextView bodyText = view.findViewById(R.id.comment);

        personText.setText(name);
        bodyText.setText(body);


        return view;
    }
}
