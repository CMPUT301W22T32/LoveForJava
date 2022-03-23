package com.example.loveforjava;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class QRcode extends AppCompatActivity{
    DocumentSnapshot document;
    FirebaseFirestore db;
    Boolean like_checker = false;
    private String codeId;  // this is just the hashed code string
    private String nickName;
    private int score;
    private int likes;
    private int flags;
    ArrayList<String> seenBy;
    ArrayList<String> loc;
    TextView seenText, likeCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodes);

        seenText = findViewById(R.id.seen_link);

        seenText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(QRcode.this, SeenBy.class);
                startActivity(intent);
            }
        });

        likeCount = findViewById(R.id.like_count);
    }

    public class QrCodeHolder {
        View mView;
        ImageButton mLikeBtn;

        public QrCodeHolder(View view){
            mView = view;
            mLikeBtn = (ImageButton) mView.findViewById(R.id.like_button);
        }

    }

    // **empty constructor requried for firebase storage**
    public QRcode(){}

    public QRcode(String name){
        codeId = name;
        nickName = name;
        score = 100;
        likes = 5;
        flags = 0;
        seenBy = new ArrayList<>();
        loc = new ArrayList<>();
    }

    public void addSeenBy(String name){
        seenBy.add(name);
    }
    public void remSeenBy(String name){
        seenBy.remove(name);
    }

    public void setNickName(String name){
        nickName = name;
    }

    public int getFlags() {
        return flags;
    }

    public int getLikes() {
        return likes;
    }

    public int getScore() {
        return score;
    }

    public String getCodeId() {
        return codeId;
    }

    public String getNickName() {
        return nickName;
    }

    public ArrayList<String> getLoc() {
        return loc;
    }

    public ArrayList<String> getSeenBy() {
        return seenBy;
    }
}
