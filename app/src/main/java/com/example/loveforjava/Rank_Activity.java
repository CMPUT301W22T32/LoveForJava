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

import java.util.ArrayList;
import java.util.Map;

public class Rank_Activity extends AppCompatActivity {
    private Player player;
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> values = new ArrayList<>();
    private ArrayAdapter Adapter;
    private final APIMain APIserver = new APIMain();
    private TextView rankView;
    // for caching data
    private ArrayList<String> usernamesSingle = new ArrayList<>();
    private ArrayList<String> usernamesTot = new ArrayList<>();
    private ArrayList<String> usernamesNum = new ArrayList<>();
    private ArrayList<String> valuesSingle = new ArrayList<>();
    private ArrayList<String> valuesTot = new ArrayList<>();
    private ArrayList<String> valuesNum = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rankView = findViewById(R.id.userRank);

        Intent i = getIntent();
        player = (Player) i.getSerializableExtra("player");

        getData("highestCode");
        Adapter = new CustomList_rank(this, usernames,values);
        ListView rankList = findViewById(R.id.rank_list);
        rankList.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();

        setListeners();
    }

    private void setListeners(){

        // TODO: implement caching

        final TextView single = findViewById(R.id.single);
        single.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(usernamesSingle.size() == 0){
                    getData("highestCode");
                    //usernamesSingle.addAll(usernames);
                    valuesSingle.addAll(values);
                }else{
                    updateAdapter(usernamesSingle, valuesSingle);
                }
            }
        });

        final TextView num_of_scans = findViewById(R.id.num_of_scans);
        num_of_scans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(usernamesNum.size() == 0){
                    getData("numScanned");
                    //usernamesNum.addAll(usernames);
                    valuesNum.addAll(values);
                }else{
                    updateAdapter(usernamesNum, valuesNum);
                }
            }
        });

        final TextView all_scans = findViewById(R.id.all_scans);
        all_scans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(usernamesTot.size() == 0){
                    getData("totScore");
                    //usernamesTot.addAll(usernames);
                    valuesTot.addAll(values);
                }else{
                    updateAdapter(usernamesTot, valuesTot);
                }
            }
        });
    }

    private void getData(String field){
        APIserver.getRank(player, field, new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (Boolean) response.get("success")){
                    usernames.clear();
                    values.clear();
                    usernames.addAll( (ArrayList<String>) response.get("usernames"));
                    values.addAll( (ArrayList<String>) response.get("values"));
                    Adapter.notifyDataSetChanged();
                    String rank = response.get("rank")+"";
                    rankView.setText("Your Rank: "+ rank);
                }else{
                    //err
                }
            }
        });
    }

    private void updateAdapter(ArrayList<String> names, ArrayList<String> vals){
        Log.i("BEFORE", usernames+"");
        Log.i("BEFORE", names+"");
        usernames.clear();
        values.clear();
        usernames.addAll(names);
        values.addAll(vals);
        Log.i("AFTER", usernames+"");
        Adapter.notifyDataSetChanged();
    }

    /*private void onDataChange(){
        usernames.addAll( (ArrayList<String>) response.get("usernames"));
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
    }*/

}