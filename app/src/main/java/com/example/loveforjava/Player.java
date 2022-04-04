package com.example.loveforjava;


import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Player implements Serializable {
    String userId;
    String userName;
    String email;
    Map<String, String> scannedCodes;  // stored as nickName:code_id
    Integer highestCode;
    Integer lowestCode;
    Integer totScore;
    Integer numScanned;


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
        numScanned = 0;
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
        String id = code.getCodeId();
        int score = code.getScore();
        if(scannedCodes.containsValue(id)){
            return false;
        }

        if(score > highestCode){
            highestCode = score;
        }
        if(score < lowestCode || lowestCode == -1){
            lowestCode = score;
        }
        totScore += score;
        scannedCodes.put(name, id);
        numScanned++;
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
        numScanned--;
        Log.i("codes", scannedCodes+"");
        return true;
    }

    public void delQRcode(QRcode code){
        for(String i : scannedCodes.keySet()){
            if(scannedCodes.get(i).equals(code.getCodeId())){
                scannedCodes.remove(i);
                break;
            }
        }
        // TODO: change highest/lowest score if this is that code
        totScore -= code.getScore();
        numScanned--;
        Log.i("codes", scannedCodes+"");
    }

    public String getQRcodeByName(String name){
        Log.i(name, scannedCodes.get(name));
        return scannedCodes.get(name);
    }

    public Integer getTotScore() {
        return totScore;
    }

    public Map<String, String> getScannedCodes() {
        return scannedCodes;
    }

    public Integer getHighestCode() {
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

    public Integer getLowestCode() {
        return lowestCode;
    }

    public Integer getNumScanned(){
        return numScanned;
    }
}
