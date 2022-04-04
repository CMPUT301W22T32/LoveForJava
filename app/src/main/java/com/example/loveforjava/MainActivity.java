package com.example.loveforjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText name;
    private Player p;
    EditText search_friends;
    Spinner friends;
    ArrayList<String> usernames = new ArrayList<>();
    APIMain user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        p = (Player) i.getSerializableExtra("PLAYER");
        setContentView(R.layout.activity_main);

        APIMain APIserver = new APIMain();

        search_friends = findViewById(R.id.username);
        friends =  findViewById(R.id.friends);

        usernames.add("uuuu");
        usernames.add("uauaua");
        usernames.add("she");
        usernames.add("he");
        usernames.add("fruit");

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,usernames);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,usernames);

        //search_friends.setAdapter(adapter);
        friends.setAdapter(adapter1);

        search_friends.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence == ""){
                    return;
                }
                APIserver.searchByUsername(charSequence + "", new ResponseCallback() {
                    @Override
                    public void onResponse(Map<String, Object> response) {
                        //usernames.clear();
                        ArrayList<Player> p = (ArrayList<Player>) response.get("data");
                        for(int i=0;i<p.size();i++){
                            usernames.set(i, p.get(i).getUserName());
                        }

                        //friends.setAdapter(adapter1);
                        Log.i("NAMES", usernames+"");
                        //adapter.notifyDataSetChanged();
                        adapter1.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

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