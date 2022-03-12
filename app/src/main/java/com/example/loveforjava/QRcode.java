package com.example.loveforjava;

import java.util.ArrayList;

public class QRcode {
    private String codeId;  // this is just the hashed code string
    private String nickName;
    private int score;
    private int likes;
    private int flags;
    ArrayList<String> seenBy;
    ArrayList<String> loc;

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
