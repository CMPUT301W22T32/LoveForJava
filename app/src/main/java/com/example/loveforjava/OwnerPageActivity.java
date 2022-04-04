package com.example.loveforjava;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OwnerPageActivity extends AppCompatActivity {
    APIMain APIserver = new APIMain();
    ListView qrList;
    ListView playerList;
    ArrayAdapter qrAdapter;
    ArrayAdapter playerAdapter;
    ArrayList<QRcode> qrCodes;
    ArrayList<Player> players;
    ArrayList<String> qrCodesStrings = new ArrayList<String>();
    ArrayList<String> userNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_page);

        qrList = findViewById(R.id.qr_code_list);
        playerList = findViewById(R.id.player_list);

        final TextView qr_btn = findViewById(R.id.qr_codes);
        final TextView player_btn = findViewById(R.id.players);

        // Display qrList first and hide playerList
        qrList.setVisibility(View.VISIBLE);
        playerList.setVisibility(View.GONE);

        qrAdapter = new CustomList(this, qrCodesStrings);
        qrList.setAdapter(qrAdapter);
        qrAdapter.notifyDataSetChanged();

        APIserver.getAllCodes(new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (Boolean) response.get("success")) {
                    qrCodes = (ArrayList<QRcode>) response.get("data");         //Issue here!
                    for (int i=0; i<qrCodes.size(); i++) {
                        qrCodesStrings.add(qrCodes.get(i).getCodeId());
                    }
                    qrAdapter.notifyDataSetChanged();
                }else{
                    // err
                }
            }
        });

        qrList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteConfirmationQr(i);
                return true;
            }
        });

        playerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteConfirmationQr(i);
                return false;
            }
        });

        player_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                qrList.setVisibility(View.GONE);
                playerList.setVisibility(View.VISIBLE);
                APIserver.getAllUsers(new ResponseCallback() {
                    @Override
                    public void onResponse(Map<String, Object> response) {

                    }
                });
                playerAdapter = new CustomList(getApplicationContext(), userNames);
                playerList.setAdapter(playerAdapter);
                playerAdapter.notifyDataSetChanged();
            }
        });

        qr_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                qrList.setVisibility(View.VISIBLE);
                playerList.setVisibility(View.GONE);
                APIserver.getAllCodes(new ResponseCallback() {
                    @Override
                    public void onResponse(Map<String, Object> response) {

                    }
                });
                qrAdapter = new CustomList(getApplicationContext(), qrCodesStrings);
                qrList.setAdapter(qrAdapter);
                playerAdapter.notifyDataSetChanged();
            }
        });

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
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteQRcode(int i){
        Log.i("POS", i+"");
        QRcode qr = qrCodes.get(i);
        APIserver.deleteCodeFromDB(qr.getCodeId(), new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (boolean) response.get("success")){
                    Toast.makeText(getApplicationContext(), "QR code deleted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Could not delete QR code at this time, please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}