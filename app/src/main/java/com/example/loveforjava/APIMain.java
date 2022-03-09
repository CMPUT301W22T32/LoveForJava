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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
        codes = db.collection("QR_codes");
        comments = db.collection("comments");
        //images = db.collection("images");
    }

    public void createPlayer(String name, String email, ResponseCallback responseCallback){
        // NOTE: inside the db the userId will always be stored as null, that because we store this locally
        Map<String, Object> res = new HashMap<>();
        Player player = new Player(name, email);
        players.add(player)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        res.put("success", true);
                        res.put("user_id", documentReference.getId()); // type: String
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        res.put("success", false);
                        res.put("err", "" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void getPlayerInfo(String user_id, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        players.document(user_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            res.put("success", true);
                            Player player = documentSnapshot.toObject(Player.class);
                            player.setUserId(documentSnapshot.getId());
                            res.put("Player_obj", player); // type: Player
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
                        res.put("err", "" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void updatePlayerInfo(Player player, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        players.document(player.getUserId()).set(player)
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
                        res.put("err", ":" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void addQRCode(QRcode qr_code, Player player, ResponseCallback responseCallback){
        // TODO: make this into a map object so that we can store the code(or nickname) along with the score
        Map<String, Object> res = new HashMap<>();
        String id = qr_code.getCodeId();
        // if code already scanned
        if(!player.addQR(qr_code.getNickName(), id)){
            res.put("success", false);
            res.put("err", "QR code already scanned: "+qr_code.getNickName());
            responseCallback.onResponse(res);
            return;
        }
        qr_code.addSeenBy(player.getUserName());
        players.document(player.getUserId()).set(player)
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
                res.put("success", false);
                res.put("err", "" + e);
                responseCallback.onResponse(res);
            }
        });

        codes.document(id).set(qr_code)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "QR code info succesuffly updated");
                        res.put("success", true);
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        res.put("success", false);
                        res.put("err", "" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void delQRCode(QRcode qr_code, Player player, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        String id = qr_code.getCodeId();
        // if code already scanned
        if(!player.remQR(qr_code.getNickName())){
            res.put("success", false);
            res.put("err", "QR code '"+qr_code.getNickName()+"' does not exist");
            responseCallback.onResponse(res);
            return;
        }
        player.printPlayer();
        players.document(player.getUserId()).set(player)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        res.put("success", false);
                        res.put("err", "" + e);
                        responseCallback.onResponse(res);
                    }
                });
        qr_code.remSeenBy(player.getUserName());
        codes.document(id).set(qr_code)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "QR code info succesuffly updated");
                        res.put("success", true);
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        res.put("success", false);
                        res.put("err", "" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void getQRcode(String code_id, String nickName, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        codes.document(code_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            res.put("success", true);
                            QRcode code = documentSnapshot.toObject(QRcode.class);
                            code.setNickName(nickName);
                            res.put("QRcode_obj", code);
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
                        res.put("err", "" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }
    
    public void createComment(String code_id, String userName, String body, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        data.put("user_name", userName);
        data.put("body", body);
        data.put("code_id", code_id);
        comments.add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        res.put("success", true);
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        res.put("success", false);
                        res.put("err", "" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void getComments(String code_id, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        comments.whereEqualTo("code_id", code_id).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i(TAG, document.getId() + " => " + document.getData());
                                data.add(document.getData());
                            }
                            res.put("success", true);
                            res.put("data", data); // type: ArrayList<Map<String, Object>>
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            res.put("success", false);
                            res.put("err", "" + task.getException());
                        }
                        responseCallback.onResponse(res);
                    }
                });
    }
}
