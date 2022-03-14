package com.example.loveforjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText name;
    private Player p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        p = (Player) i.getSerializableExtra("PLAYER");
        setContentView(R.layout.activity_main);

        ImageButton Profile_Button = findViewById(R.id.profile);
        Profile_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_Activity(p);
            }
        });

        /**
         * set listener for Map_Button
         */
        ImageButton Map_Button= findViewById(R.id.location);
        Map_Button.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              openMap_Activity();
                                          }
                                      }

        );

        /**
         * set listener for Rank_Button
         */
        ImageButton Rank_Button= findViewById(R.id.rank);
        Rank_Button.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              openRank_Activity();
                                          }
                                      }

        );

        /**
         * set listener for QR_Button
         */
        Button QR_Button= findViewById(R.id.qr);
        QR_Button.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               openCodeScanner_activity();
                                           }
                                       }

        );
    }

    public void profile_Activity(Player p) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("PLAYER", p);
        startActivity(intent);
    }

    /**
     * open openMap Activity
     */
    public void openMap_Activity(){
        Intent intent = new Intent(this, Map_Activity.class);
        startActivity(intent);
    }

    /**
     * open openRank Activity
     */
    public void openRank_Activity(){
        Intent intent = new Intent(this, Rank_Activity.class);
        startActivity(intent);
    }

    /**
     * open CodeScanner_activity
     */
    public void openCodeScanner_activity(){
        Intent intent = new Intent(this, CodeScanner_activity.class);
        startActivity(intent);
    }

}