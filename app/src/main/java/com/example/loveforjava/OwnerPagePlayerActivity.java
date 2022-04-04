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

import java.util.ArrayList;
import java.util.Map;

public class OwnerPagePlayerActivity extends AppCompatActivity {
    private boolean longPress;
    APIMain APIserver = new APIMain();
    private ArrayList<QRcode> qrCodes;
    private ArrayList<String> qrCodesStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_page_player);

        longPress = false;

//        ArrayList<String> players = new ArrayList<String>();
//        //for(Map.Entry<String, String> entry: getPlayers()) {
//        //    players.add(entry.getKey());
//        //}

//        //ListView playerList = findViewById(R.id.playerList);
//        ArrayAdapter playerAdapter = new CustomList(this, players);
//        //playerList.setAdapter(playerAdapter);
//        playerAdapter.notifyDataSetChanged();

//        playerAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                longPress = true;
//                deleteConfirmationPl(i);
//                return true;
//            }
//        });

    }

//    public void deleteConfirmationPl(int i) {
//        /*  WEBSITE : https://stackoverflow.com
//         *  LINK TO SOLUTION : https://stackoverflow.com/a/36747528
//         *  AUTHOR : https://stackoverflow.com/users/5130239/dus
//         * */
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle("Confirm to Delete?");
//        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int j) {
//                Log.i("LOC", i+"");
//                deletePlayer(i);
//                longPress = false;
//            }
//        });
//        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int j) {
//                longPress = false;
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

//    private void deletePlayer(int i){
//
//    }

}