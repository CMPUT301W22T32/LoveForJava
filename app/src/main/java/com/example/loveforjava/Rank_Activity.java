package com.example.loveforjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Rank_Activity extends AppCompatActivity {
    private Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Intent i = getIntent();
        player = (Player) i.getSerializableExtra("PLAYER");

        //for(Map.Entry<String, String> entry: player.scannedCodes.entrySet()) {
        //qrName.add(entry.getKey());}
        ArrayList<String> username = new ArrayList<String>();
        ArrayList<Integer> value = new ArrayList<Integer>();
        ListView rankList = findViewById(R.id.rank_list);
        ArrayAdapter Adapter = new CustomList_rank(this, username,value);
        rankList.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();


        final TextView single = findViewById(R.id.single);
        single.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Adapter.notifyDataSetChanged();
                finish();
            }
        });

        final TextView num_of_scans = findViewById(R.id.num_of_scans);
        num_of_scans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Adapter.notifyDataSetChanged();
                finish();
            }
        });

        final TextView all_scans = findViewById(R.id.all_scans);
        all_scans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Adapter.notifyDataSetChanged();
                finish();
            }
        });
    }

}