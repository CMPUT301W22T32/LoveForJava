package com.example.loveforjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class QRcodeActivity extends AppCompatActivity {
    private Player p;
    private String codeId;
    private String codeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        p = (Player) i.getSerializableExtra("PLAYER");
        codeId = i.getStringExtra("QRcode");
        codeName = i.getStringExtra("name");
        setContentView(R.layout.activity_qrcodes);

        APIMain APIServer = new APIMain();
        APIServer.getComments(codeId, new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                ArrayList<Map<String, Object>> commentsData = new ArrayList<>();
                Map<String, Object> temp;
                ArrayList<String> userNames = new ArrayList<String>();
                ArrayList<String> commentsBody = new ArrayList<String>();
                commentsData = (ArrayList<Map<String, Object>>) response.get("data");
                for(int i = 0 ; i < commentsData.size(); i++) {
                    temp = (Map<String, Object>) commentsData.get(i);
                    userNames.add((String) temp.get("user_name"));
                    commentsBody.add((String) temp.get("body"));
                }
                populateCommentList(userNames, commentsBody);
            }
        });
    }

    public void populateCommentList(ArrayList<String> userNames, ArrayList<String> commentsBody) {
        ListView commentList = findViewById(R.id.comment_list);
        ArrayAdapter commentAdapter = new CustomList2(this, userNames, commentsBody);
        commentList.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();
    }
}