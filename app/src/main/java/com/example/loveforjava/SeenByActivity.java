package com.example.loveforjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SeenByActivity extends AppCompatActivity {
    private ArrayList<String> userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen_by);

        Intent i = getIntent();
        userName = (ArrayList<String>) i.getSerializableExtra("Seen By");


        ListView nameList = findViewById(R.id.qr_list);
        ArrayAdapter nameAdapter = new CustomList(this, userName);
        nameList.setAdapter(nameAdapter);
        nameAdapter.notifyDataSetChanged();
    }
}