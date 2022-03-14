package com.example.loveforjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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