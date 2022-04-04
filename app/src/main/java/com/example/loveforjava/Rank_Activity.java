package com.example.loveforjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class Rank_Activity extends AppCompatActivity {
    ArrayList<String> usernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        Intent i = getIntent();
        Player p = (Player) i.getSerializableExtra("player");

        usernames =  new ArrayList<>();
        ArrayList<String> values =  new ArrayList<>();

        APIMain APIserver = new APIMain();
        APIserver.getRank(p, "highestCode", new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (Boolean) response.get("success")){
                    usernames.addAll( (ArrayList<String>) response.get("usernames"));
                    values.addAll( (ArrayList<String>) response.get("values"));
                }else{
                    //err
                }
            }
        });
    }

    private void onDataChange(){
        usernames.addAll( (ArrayList<String>) response.get("usernames"));
    }
}
