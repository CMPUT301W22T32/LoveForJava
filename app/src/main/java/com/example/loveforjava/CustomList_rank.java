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

public class CustomList_rank extends ArrayAdapter<String> {
    private ArrayList<String> userNames;
    private ArrayList<String> value;
    private Context context;

    public CustomList_rank(Context context, ArrayList<String> userNames, ArrayList<String> value) {
        super(context, 0, userNames);
        this.userNames = userNames;
        this.value = value;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.rank_list, parent, false);
        }

        String name = userNames.get(position);
        String Value = value.get(position);

        TextView personText = view.findViewById(R.id.personName);
        TextView valueText = view.findViewById(R.id.value);

        personText.setText(name);
        valueText.setText(Value);


        return view;
    }
}