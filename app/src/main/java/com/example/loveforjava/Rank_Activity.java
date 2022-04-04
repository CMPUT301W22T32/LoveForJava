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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class Rank_Activity extends AppCompatActivity {
    private Player player;
    private ArrayList<String> username;
    private ArrayList<String> value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Intent i = getIntent();
        Player p= (Player) i.getSerializableExtra("PLAYER");

        username = new ArrayList<String>();
        for(Map.Entry<String, String> entry: player.userName.entrySet()) {
            username.add(entry.getKey());}

        value = new ArrayList<String>();
        for(Map.Entry<String, Integer> entry: player.value.entrySet()) {
            value.add(entry.getKey());}

        ListView rankList = findViewById(R.id.rank_list);
        ArrayAdapter Adapter = new CustomList_rank(this, username,value);
        rankList.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();


        APIMain APIserver = new APIMain();
        Log.i("player", p+"");
        APIserver.getRank(p, "highestCode", new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if((Boolean) response.get("success")){
                    Log.i("RANK", response.get("rank")+"");
                    Log.i("RANK", response.get("usernames")+"");
                    Log.i("RANK", response.get("values")+"");
                }
            }
        });


        final TextView single = findViewById(R.id.single);
        single.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username.clear();
                value.clear();
                //datafilling

                Adapter.notifyDataSetChanged();
                finish();
            }
        });

        final TextView num_of_scans = findViewById(R.id.num_of_scans);
        num_of_scans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username.clear();
                value.clear();
                //datafilling


                Adapter.notifyDataSetChanged();
                finish();
            }
        });

        final TextView all_scans = findViewById(R.id.all_scans);
        all_scans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username.clear();
                value.clear();
                //datafilling


                Adapter.notifyDataSetChanged();
                finish();
            }
        });
    }

}