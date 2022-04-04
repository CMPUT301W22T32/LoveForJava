package com.example.loveforjava;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.Map;

/**
 * This class allows user to search for other players, open map, open rank, and open own profile
 */
public class MainActivity extends AppCompatActivity {
    Player p;
    EditText searchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        p = (Player) i.getSerializableExtra("PLAYER");
        setContentView(R.layout.activity_main);

        searchUser = findViewById(R.id.username);
        searchUser.setImeActionLabel("Custom Text", KeyEvent.KEYCODE_ENTER);
        searchUser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.i("KEY", i+"");
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    Log.i("HERE", searchUser.getText() + "");
                    APIMain APIServer = new APIMain();
                    APIServer.searchByUsername(searchUser.getText() + "", new ResponseCallback() {
                        @Override
                        public void onResponse(Map<String, Object> response) {
                            if((Boolean) response.get("success")) {
                                Player searchedPlayer = (Player) response.get("Player_obj");
                                if(searchedPlayer == null){
                                    Toast.makeText(MainActivity.this, "No Player named found", Toast.LENGTH_LONG).show();
                                }else{
                                    Intent intent = new Intent(MainActivity.this, OtherUserActivity.class);
                                    intent.putExtra("PLAYER", searchedPlayer);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                }
                return false;
            }
        });



        /**
         * set listener for profile_Button
         */
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

    /**
     * open ProfileActivity
     * @param p
     */
    public void profile_Activity(Player p) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("PLAYER", p);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                p = (Player) data.getSerializableExtra("result");
            }
        }
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
        intent.putExtra("player", p);
        startActivity(intent);
    }

    /**
     * open CodeScanner_activity
     */
    public void openCodeScanner_activity(){
        Intent intent = new Intent(this, CodeScanner_activity.class);
        intent.putExtra("player", p);
        intent.putExtra("Previous Activity", "Main");
        startActivity(intent);
    }

}