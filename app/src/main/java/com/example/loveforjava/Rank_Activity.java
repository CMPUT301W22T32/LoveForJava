package com.example.loveforjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class Rank_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        Intent i = getIntent();
        Player p = (Player) i.getSerializableExtra("player");

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
    }
}
