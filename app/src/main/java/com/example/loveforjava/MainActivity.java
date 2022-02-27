package com.example.loveforjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APIMain APIServer = new APIMain();
        //Map<String, Object>  res = APIServer.createPlayer("jasper", "jleng1@ualberta.ca");
        //assertEquals(true, res.get("success"));
        //String id  =(String) res.get("user_id");
    }
}