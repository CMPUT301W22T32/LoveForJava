package com.example.loveforjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.api.AuthProviderOrBuilder;

import java.util.Map;

public class QRcodeActivity extends AppCompatActivity {
    private Player p;
    private String codeId;
    private String codeName;
    private ImageButton submit;
    private EditText comment;
    private TextView score;
    private QRcode code;
    private APIMain APIserver;

    public void submit(View v){
        APIserver.createComment(codeId, p.getUserName(), comment.getText()+"", new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (boolean) response.get("success")){
                    Log.i("SUCC", "ESS");
                    Toast.makeText(QRcodeActivity.this, "Comment addded", Toast.LENGTH_SHORT).show();
                    comment.setText("");
                }else{
                    Log.i("FAIL", "IURE");
                    Toast.makeText(QRcodeActivity.this, "Comment could not be added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        APIserver = new APIMain();
        Intent i = getIntent();
        p = (Player) i.getSerializableExtra("PLAYER");
        codeId = i.getStringExtra("QRcode");
        codeName = i.getStringExtra("name");
        setContentView(R.layout.activity_qrcodes);

        comment = findViewById(R.id.comment_text);
        submit = findViewById(R.id.submit_comment);
        score = findViewById(R.id.score);

        APIserver.getQRcode(codeId, codeName, new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (boolean) response.get("success")){
                    Log.i("SUCC", "ESS");
                    code = (QRcode) response.get("QRcode_obj");
                    runActivity();
                }else{
                    Log.i("FAIL", "IURE");
                }
            }
        });

    }

    private void runActivity(){
        score.setText(codeName +"\n Score:"+ code.getScore());
    }



}
