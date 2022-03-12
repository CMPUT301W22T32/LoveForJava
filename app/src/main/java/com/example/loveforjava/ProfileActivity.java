package com.example.loveforjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        player = (Player) i.getSerializableExtra("PLAYER");

        TextView highest_box = (TextView)findViewById(R.id.highest_box);
        highest_box.setText(player.getHighestCode().getScore());
        TextView total_box = (TextView)findViewById(R.id.total_box);
        total_box.setText(player.getTotScore());
        TextView no_scans_box = (TextView)findViewById(R.id.no_scans_box);
        no_scans_box.setText(player.getScannedCodes().size());
        //TextView lowest_box = (TextView)findViewById(R.id.lowest_box);
        //lowest_box.setText(player.getLowestCode().getScore());            Don't have a straightforward function to get lowest score

    }
}