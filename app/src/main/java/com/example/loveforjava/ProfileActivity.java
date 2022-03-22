package com.example.loveforjava;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        player = (Player) i.getSerializableExtra("PLAYER");

        ArrayList<String> qrName = new ArrayList<String>();
        for(Map.Entry<String, String> entry: player.scannedCodes.entrySet()) {
            qrName.add(entry.getKey());
        }
        TextView generateProfileQR = findViewById(R.id.generate_qrcode);
        ListView qrList = findViewById(R.id.qr_list);
        ArrayAdapter qrAdapter = new CustomList(this, qrName);
        qrList.setAdapter(qrAdapter);
        qrAdapter.notifyDataSetChanged();
        qrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int i, long id) {
                // TODO Auto-generated method stub
                Log.i("############","Items " + i);
                String qrId = player.scannedCodes.get(qrName.get(i));
                Intent intent = new Intent(ProfileActivity.this, QRcodeActivity.class);
                intent.putExtra("PLAYER", player);
                intent.putExtra("QRcode", qrId);
                intent.putExtra("name", qrName.get(i));
                startActivity(intent);
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
        lowest_box.setText(Integer.toString(player.getLowestCode()));

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
                finish();
            }
        });
    }
}