package com.example.loveforjava;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class OwnerPageActivity extends AppCompatActivity {
    private boolean longPress;
    APIMain APIserver = new APIMain();
    private ArrayList<QRcode> qrCodes;
    private ArrayList<String> qrCodesStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_page);

        longPress = false;

//        ArrayList<String> players = new ArrayList<String>();
//        //for(Map.Entry<String, String> entry: getPlayers()) {
//        //    players.add(entry.getKey());
//        //}

        APIserver.getAllCodes(new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (Boolean) response.get("success")) {
                    qrCodes = (ArrayList<QRcode>) response.get("data");         //Issue here!
        }}});

        for (int i=0; i<qrCodes.size(); i++) {                                  //Issue here!
            qrCodesStrings.add(String.valueOf(qrCodes.get(i).getFlags()));
        }

        ListView qrList = findViewById(R.id.qr_code_list);
        ArrayAdapter qrAdapter = new CustomList(this, qrCodesStrings);
        qrList.setAdapter(qrAdapter);
        qrAdapter.notifyDataSetChanged();

//        //ListView playerList = findViewById(R.id.playerList);
//        ArrayAdapter playerAdapter = new CustomList(this, players);
//        //playerList.setAdapter(playerAdapter);
//        playerAdapter.notifyDataSetChanged();

        qrList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                longPress = true;
                deleteConfirmationQr(i);
                return true;
            }
        });

//        playerAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                longPress = true;
//                deleteConfirmationPl(i);
//                return true;
//            }
//        });

    }

    public void deleteConfirmationQr(int i) {
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
                deleteQRcode(i);
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

    private void deleteQRcode(int i){
        Log.i("POS", i+"");
        QRcode qr = qrCodes.get(i);
        //String id = player.getQRcodeByName(name);
        //Log.i("player", name+":"+id);
//        APIserver.delQRCode((QRcode) qr, player, new ResponseCallback() {
//            @Override
//            public void onResponse(Map<String, Object> response) {
//                if( (boolean) response.get("success")){
//                    player = (Player) response.get("Player_obj");
//                    qrName.remove(i);
//                    qrAdapter.notifyDataSetChanged();
//                }else{
//                    Toast.makeText(getApplicationContext(), "Cannot delete QR code", Toast.LENGTH_SHORT).show();
//                    }
//            }
//        });
    }

//    private void deletePlayer(int i){
//
//    }

}