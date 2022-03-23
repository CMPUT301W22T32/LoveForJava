package com.example.loveforjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class OtherUserActivity extends AppCompatActivity {
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        Intent i = getIntent();
        player = (Player) i.getSerializableExtra("PLAYER");

        ArrayList<String> qrName = new ArrayList<String>();
        for(Map.Entry<String, String> entry: player.scannedCodes.entrySet()) {
            qrName.add(entry.getKey());
        }
        ListView qrList = findViewById(R.id.qr_list);
        ArrayAdapter qrAdapter = new CustomList(this, qrName);
        qrList.setAdapter(qrAdapter);
        qrAdapter.notifyDataSetChanged();

        TextView highest_box = (TextView)findViewById(R.id.highest_box);
        highest_box.setText(Integer.toString(player.getHighestCode()));
        TextView total_box = (TextView)findViewById(R.id.total_box);
        total_box.setText(Integer.toString(player.getTotScore()));
        TextView no_scans_box = (TextView)findViewById(R.id.no_scans_box);
        no_scans_box.setText(Integer.toString(player.getScannedCodes().size()));
        TextView lowest_box = (TextView)findViewById(R.id.lowest_box);
        lowest_box.setText(Integer.toString(player.getLowestCode()));

        final ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}