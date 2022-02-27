package com.example.loveforjava;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class APIMain {
    private String TAG = "API server";
    private FirebaseFirestore db;
    private CollectionReference players;
    private CollectionReference codes;
    private CollectionReference comments;
    //private CollectionReference images;

    public APIMain(){
        db = FirebaseFirestore.getInstance();
        Log.i("db", db+"");
        players = db.collection("players");
        codes = db.collection("codes");
        comments = db.collection("comments");
        //images = db.collection("images");
    }

    public void createPlayer(String name, String email, ResponseCallback responseCallback){
        // TODO: possibly make this a rereviable class like how QR code is set up
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("scanned_codes", Arrays.asList());
        final String[] id = new String[1];
        players.add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        res.put("success", true);
                        res.put("user_id", documentReference.getId());
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        res.put("success", false);
                        res.put("err", "Data could not be fetched!" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void getPlayerInfo(String user_id, ResponseCallback responseCallback){
        // TODO: possibly make this a rereviable class like how QR code is set up
        Map<String, Object> res = new HashMap<>();
        players.document(user_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.i("hereh","here");
                        //DocumentSnapshot document = task.getResult();
                        if (documentSnapshot.exists()) {
                            Log.i(TAG, "DocumentSnapshot data: " + documentSnapshot.getData().get("email"));
                            res.put("success", true);
                            res.put("data", documentSnapshot.getData());
                        } else {
                            Log.d(TAG, "No such document");
                            res.put("success", false);
                            res.put("err", "No such document");
                            //responseCallback.onResponse(res);
                        }
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Data could not be fetched!" + e);
                        res.put("success", false);
                        res.put("err", "Data could not be fetched:" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void updatePlayerInfo(String user_id, Map<String, Object> data, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        players.document(user_id).set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "player info succesuffly updated");
                        res.put("success", true);
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        res.put("success", false);
                        res.put("err", "Data could not be updated:" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void addQRCode(QRcode qr_code, String user_id, ResponseCallback responseCallback){
        // TODO: make this into a map object so that we can store the code(or nickname) along with the score
        Map<String, Object> res = new HashMap<>();
        String id = qr_code.getCode_id();
        players.document(user_id).update("scanned_codes", FieldValue.arrayUnion(id));
        codes.document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            Log.i("Document data:", "document exists");
                        } else {
                            codes.document().set(qr_code);
                            codes.document().update("seen_by", user_id); // eventually this will be nickname
                        }
                        if(task.isSuccessful()){
                            res.put("success", true);
                        }else{
                            res.put("success", false);
                        }
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void delQRCode(String hased_code, String user_id, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        players.document(user_id).update("scanned_codes", FieldValue.arrayRemove(hased_code))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "QRcode succesuffly removed");
                        res.put("success", true);
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error removing document", e);
                        res.put("success", false);
                        res.put("err", "Data could not be deleted:" + e);
                        responseCallback.onResponse(res);
                    }
                });
        // TODO: remove player from QR code "seen by list"
    }

    public void getQRcode(String code_id, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        codes.document(code_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            res.put("success", true);
                            res.put("QRcode_obj", documentSnapshot.toObject(QRcode.class));
                        } else {
                            Log.d(TAG, "No such document");
                            res.put("success", false);
                            res.put("err", "No such document");
                        }
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Data could not be fetched!" + e);
                        res.put("success", false);
                        res.put("err", "Data could not be fetched:" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }
}
