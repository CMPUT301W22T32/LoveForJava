package com.example.loveforjava;

import java.util.ArrayList;

public class QRcode {
    private String code_id;  // this is just the hashed code string
    private String nickname;
    private int score;
    private int likes;
    private int flags;
    ArrayList<String> seen_by;
    ArrayList<String> loc;

    // **empty constructor requried for firebase storage**
    public QRcode(){}

    public int getFlags() {
        return flags;
    }

    public int getLikes() {
        return likes;
    }

    public int getScore() {
        return score;
    }

    public String getCode_id() {
        return code_id;
    }

    public String getNickname() {
        return nickname;
    }

    public ArrayList<String> getLoc() {
        return loc;
    }

    public ArrayList<String> getSeen_by() {
        return seen_by;
    }
}
