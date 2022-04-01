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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_page);

        longPress = false;

        ArrayList<String> players = new ArrayList<String>();
        //for(Map.Entry<String, String> entry: getPlayers()) {
        //    players.add(entry.getKey());
        //}

        ArrayList<String> qrCodes = new ArrayList<String>();
        //for(Map.Entry<String, String> entry: getQrCodes()) {
        //    qrCodes.add(entry.getKey());
        //}

        ListView qrList = findViewById(R.id.qr_list2);
        ArrayAdapter qrAdapter = new CustomList(this, qrCodes);
        qrList.setAdapter(qrAdapter);
        qrAdapter.notifyDataSetChanged();

        //ListView playerList = findViewById(R.id.playerList);
        ArrayAdapter playerAdapter = new CustomList(this, players);
        //playerList.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();

        qrList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                longPress = true;
                deleteConfirmation(i);
                return true;
            }
        });

        playerAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                longPress = true;
                deleteConfirmation(i);
                return true;
            }
        });

    }

    public void deleteConfirmation(int i) {
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

    private void deleteQRcode(int i){
        Log.i("POS", i+"");
        String name = qrName.get(i);
        String id = player.getQRcodeByName(name);
        Log.i("player", name+":"+id);
        APIMain APIserver = new APIMain();
        APIserver.getQRcode(id, name, new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (boolean) response.get("success")){
                    APIserver.delQRCode((QRcode) response.get("QRcode_obj"), player, new ResponseCallback() {
                        @Override
                        public void onResponse(Map<String, Object> response) {
                            if( (boolean) response.get("success")){
                                player = (Player) response.get("Player_obj");
                                qrName.remove(i);
                                qrAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(getApplicationContext(), "Cannot delete QR code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Cannot delete QR code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}