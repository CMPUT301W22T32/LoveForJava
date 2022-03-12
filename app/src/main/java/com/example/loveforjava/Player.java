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
    QRcode highestCode;
    int totScore;


    // **empty constructor requried for firebase storage**
    public Player(){}

    public Player(String name, String Email){
        userId = null;
        userName = name;
        email = Email;
        scannedCodes = new HashMap<>();
        highestCode = null;
        totScore = 0;
    }

    public void printPlayer(){
        Log.i("userId", userId);
        Log.i("userName", userName);
        Log.i("email", email);
        Log.i("scannedCodes", scannedCodes+"");
        Log.i("highestCode", highestCode+"");
        Log.i("totScore", totScore+"");
    }

    public void setUserId(String id){
        userId = id;
    }

    public void setUserName(String name){
        userName = name;
    }

    public Boolean addQR(String nickName, String codeId){
        if(scannedCodes.containsKey(nickName)){
            return false;
        }
        scannedCodes.put(codeId, nickName);
        return true;
    }
    public Boolean remQR(String nickName){
        if(!scannedCodes.containsKey(nickName)){
            return false;
        }
        scannedCodes.remove(nickName);
        Log.i("codes", scannedCodes+"");
        return true;
    }

    public int getTotScore() {
        return totScore;
    }

    public Map<String, String> getScannedCodes() {
        return scannedCodes;
    }

    public QRcode getHighestCode() {
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
}
