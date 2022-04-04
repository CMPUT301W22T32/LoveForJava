package com.example.loveforjava;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class OwnerPagePlayerActivity extends AppCompatActivity {
    private boolean longPress;
    APIMain APIserver = new APIMain();
    private ArrayList<Player> players;
    private ArrayList<String> playerStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_page_player);

        longPress = false;

        APIserver.getAllUsers(new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (Boolean) response.get("success")) {
                    players = (ArrayList<Player>) response.get("data");         //Issue here!
                }}});

        for (int i=0; i<players.size(); i++) {                                  //Issue here!
            playerStrings.add(String.valueOf(players.get(i).getUserName()));
        }

        ListView playerList = findViewById(R.id.player_list);
        ArrayAdapter playerAdapter = new CustomList(this, playerStrings);
        playerList.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();

        playerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                longPress = true;
                deleteConfirmationPl(i);
                return true;
            }
        });

    }

    public void deleteConfirmationPl(int i) {
        /*  WEBSITE : https://stackoverflow.com
         *  LINK TO SOLUTION : https://stackoverflow.com/a/36747528
         *  AUTHOR : https://stackoverflow.com/users/5130239/dus
         * */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm to Delete?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                Log.i("LOC", i+"");
                deletePlayer(i);
                longPress = false;
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                longPress = false;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deletePlayer(int i){
        String playerID = players.get(i).getUserId();
        APIserver.deletePlayerFromDB(playerID, new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (Boolean) response.get("success")) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }}});
    }

}