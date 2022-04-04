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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private Player player;
    private ArrayList<String> qrName;
    private ArrayAdapter qrAdapter;
    private boolean longPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        longPress = false;

        Intent i = getIntent();
        player = (Player) i.getSerializableExtra("PLAYER");

        qrName = new ArrayList<String>();
        for(Map.Entry<String, String> entry: player.scannedCodes.entrySet()) {
            qrName.add(entry.getKey());
        }
        TextView userName = findViewById(R.id.profile_username);
        TextView generateProfileQR = findViewById(R.id.generate_qrcode);
        ListView qrList = findViewById(R.id.qr_list);

        userName.setText(player.getUserName());
        qrAdapter = new CustomList(this, qrName);
        qrList.setAdapter(qrAdapter);
        qrAdapter.notifyDataSetChanged();
        qrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int i, long id) {
                // TODO Auto-generated method stub
                Log.i("ORESS", longPress+"");
                if(!longPress) {
                    Log.i("############", "Items " + i);
                    String qrId = player.scannedCodes.get(qrName.get(i));
                    Intent intent = new Intent(ProfileActivity.this, QRcodeActivity.class);
                    intent.putExtra("PLAYER", player);
                    intent.putExtra("QRcode", qrId);
                    intent.putExtra("name", qrName.get(i));
                    startActivity(intent);
                }
            }

        });

        qrList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                longPress = true;
                deleteConfirmation(i);
                return true;
            }
        });

        generateProfileQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, GenerateQRCodeActivity.class);
                intent.putExtra("USERID", player.getUserId());
                startActivity(intent);
            }
        });

        TextView highest_box = (TextView)findViewById(R.id.highest_box);
        highest_box.setText(Integer.toString(player.getHighestCode()));
        TextView total_box = (TextView)findViewById(R.id.total_box);
        total_box.setText(Integer.toString(player.getTotScore()));
        TextView no_scans_box = (TextView)findViewById(R.id.no_scans_box);
        no_scans_box.setText(Integer.toString(player.getScannedCodes().size()));
        TextView lowest_box = (TextView)findViewById(R.id.lowest_box);
        if (player.getLowestCode() == -1){
            lowest_box.setText("N/A");
        }
        else {
            lowest_box.setText(Integer.toString(player.getLowestCode()));
        }

        final ImageButton camera_button = findViewById(R.id.start_camera_btn);
        camera_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CodeScanner_activity.class);
                intent.putExtra("Previous Activity", "Profile");
                startActivity(intent);
            }
        });

        final ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",player);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
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
                                Log.i("ERROR", response.get("err")+"");
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