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

public class CustomList extends ArrayAdapter<String> {
    private ArrayList<String> qrNames;
    private Context context;

    public CustomList(Context context, ArrayList<String> qrNames) {
        super(context, 0, qrNames);
        this.qrNames = qrNames;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_of_friends, parent, false);
        }

        String qrName = qrNames.get(position);

        TextView qrText = view.findViewById(R.id.QRName);

        qrText.setText(qrName);

        return view;
    }
}
