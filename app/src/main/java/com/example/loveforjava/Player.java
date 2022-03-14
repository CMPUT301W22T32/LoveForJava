package com.example.loveforjava;


import android.util.Log;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable {
    String userId;
    String userName;
    String email;
    Map<String, String> scannedCodes;  // stored as nickName:code_id
    int highestCode;
    int lowestCode;
    int totScore;


    // **empty constructor requried for firebase storage**
    public Player(){}

    public Player(String name, String Email){
        userId = null;
        userName = name;
        email = Email;
        scannedCodes = new HashMap<>();
        highestCode = 0;
        lowestCode = -1;
        totScore = 0;
    }

    public void printPlayer(){
        Log.i("userId", userId);
        Log.i("userName", userName);
        Log.i("email", email);
        Log.i("scannedCodes", scannedCodes+"");
        Log.i("highestCode", highestCode+"");
        Log.i("lowestCode", lowestCode+"");
        Log.i("totScore", totScore+"");
    }

    public void setUserId(String id){
        userId = id;
    }

    public void setUserName(String name){
        userName = name;
    }

    public Boolean addQR(QRcode code){
        String name = code.getNickName();
        int score = code.getScore();
        if(scannedCodes.containsKey(name)){
            return false;
        }

        if(score > highestCode){
            highestCode = score;
        }
        if(score < lowestCode || lowestCode == -1){
            lowestCode = score;
        }
        totScore += score;
        scannedCodes.put(name, code.getCodeId());
        return true;
    }
    public Boolean remQR(QRcode code){
        String name = code.getNickName();
        if(!scannedCodes.containsKey(name)){
            return false;
        }
        // TODO: change highest/lowest score if this is that code
        totScore -= code.getScore();
        scannedCodes.remove(name);
        Log.i("codes", scannedCodes+"");
        return true;
    }

    public int getTotScore() {
        return totScore;
    }

    public Map<String, String> getScannedCodes() {
        return scannedCodes;
    }

    public int getHighestCode() {
        return highestCode;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getLowestCode() {
        return lowestCode;
    }
}