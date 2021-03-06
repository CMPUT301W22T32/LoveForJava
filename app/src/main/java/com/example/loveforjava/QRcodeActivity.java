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

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.Map;

/**
 * Displays the information of the selected QR code
 */
public class QRcodeActivity extends AppCompatActivity {
    private Player p;
    private String codeId;
    private String codeName;
    private ImageButton submit;
    private EditText comment;
    private TextView score;
    private QRcode code;
    private APIMain APIserver;
    private ArrayAdapter commentAdapter;
    private ArrayList<String> userNames;
    private ArrayList<String> commentsBody;
    private String prevActivity;

    /**
     * Allow user to add comment
     * @param v
     */
    public void submit(View v){
        APIserver.createComment(codeId, p.getUserName(), comment.getText()+"", new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (boolean) response.get("success")){
                    Log.i("SUCC", "ESS");
                    Toast.makeText(QRcodeActivity.this, "Comment addded", Toast.LENGTH_SHORT).show();
                    userNames.add(p.getUserName());
                    commentsBody.add(comment.getText()+"");
                    commentAdapter.notifyDataSetChanged();
                    comment.setText("");
                }else{
                    Log.i("FAIL", "IURE");
                    Toast.makeText(QRcodeActivity.this, "Comment could not be added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Allow user to see the pictures associated to the Qr code
     * @param v
     */
    public void openGallery(View v){
        Intent intent = new Intent(this, ViewImages.class);
        intent.putExtra("code_id", codeId);
        startActivity(intent);
    }

    /**
     * Allow users to see who have seen the QR code
     * @param v
     */
    public void seenByFrag(View v){
        Log.i("HERE", "HERE");

        Intent intent = new Intent(this, SeenByActivity.class);
        intent.putExtra("Seen By", code.getSeenBy());
        startActivity(intent);
        Log.i("HERE", "HOWW");
        SeenByFragment fragment = new SeenByFragment();
        Bundle bundle = new Bundle();
        ArrayList<String> seenBy = code.getSeenBy();
    }

    /**
     * Directs user to the location of the QR code on the map
     * @param v
     */
    public void gotoMap(View v){
        if(code.getLoc().isEmpty()){
            Toast.makeText(this, "Code has no location to go to", Toast.LENGTH_LONG).show();
            return;
        }else{
            Intent intent = new Intent(this, Map_Activity.class);
            intent.putExtra("long", code.getLoc().get(0));
            intent.putExtra("lat", code.getLoc().get(1));
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        APIserver = new APIMain();
        Intent i = getIntent();
        p = (Player) i.getSerializableExtra("PLAYER");
        codeId = i.getStringExtra("QRcode");
        codeName = i.getStringExtra("name");
        prevActivity = i.getStringExtra("previousActivity");
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

    /**
     * Get comments of the QR code from database
     */
    private void runActivity(){
        score.setText(codeName +"\n Score:"+ code.getScore());
        APIserver.getComments(codeId, new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                ArrayList<Map<String, Object>> commentsData = new ArrayList<>();
                Map<String, Object> temp;
                userNames = new ArrayList<String>();
                commentsBody = new ArrayList<String>();
                commentsData = (ArrayList<Map<String, Object>>) response.get("data");
                for(int i = 0 ; i < commentsData.size(); i++) {
                    temp = (Map<String, Object>) commentsData.get(i);
                    userNames.add((String) temp.get("user_name"));
                    commentsBody.add((String) temp.get("body"));
                }
                populateCommentList();
            }
        });
    }

    /**
     * Populate the QR code with comments and display them
     */
    public void populateCommentList() {
        ListView commentList = findViewById(R.id.comment_list);
        commentAdapter = new CustomList2(this, userNames, commentsBody);
        commentList.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * Go to the Main Menu
     */
    @Override
    public void onDestroy(){
        Log.i("PREV", "AfterScanActivity".equals(prevActivity)+"");
        if("AfterScanActivity".equals(prevActivity)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("PLAYER", p);
            startActivity(intent);
        }else{
            super.onDestroy();
        }
    }
}
