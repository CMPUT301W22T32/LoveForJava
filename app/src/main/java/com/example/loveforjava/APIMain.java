package com.example.loveforjava;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

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
        // NOTE: inside the db the userId will always be stored as null, that's because we store this locally
        Map<String, Object> res = new HashMap<>();
        players.whereEqualTo("userName", name).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.i("Username taken", queryDocumentSnapshots.isEmpty()+"");
                        if(queryDocumentSnapshots.isEmpty()) {
                            Player player = new Player(name, email);
                            addPlayer(player, responseCallback);
                        }else{
                            res.put("success", false);
                            res.put("taken", true);
                            responseCallback.onResponse(res);
                        }
                    }
                });
    }

    private void addPlayer(Player player, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
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
        // TODO: fix seen by feature
        Map<String, Object> res = new HashMap<>();
        String id = qr_code.getCodeId();
        // if code already scanned
        if(!player.addQR(qr_code)){
            res.put("success", false);
            res.put("err", "QR code already scanned: "+qr_code.getNickName());
            responseCallback.onResponse(res);
            return;
        }
        players.document(player.getUserId()).set(player)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        player.remQR(qr_code);
                        res.put("success", false);
                        res.put("err", "" + e);
                        responseCallback.onResponse(res);
                    }
                });
        qr_code.addSeenBy(player.getUserName());
        codes.document(id).update("seenBy", FieldValue.arrayUnion(player.getUserName()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "DocumentSnapshot successfully updated!");
                        res.put("success", true);
                        res.put("Player_obj", player);
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Error updating document", e);
                        if(e.getMessage().startsWith("NOT_FOUND")){
                            Log.i(TAG, "NEW CODE");
                            addNewCode(qr_code, player, res, responseCallback);
                        }else{
                            player.remQR(qr_code);
                            res.put("success", false);
                            res.put("err", "" + e);
                            responseCallback.onResponse(res);
                        }
                    }
                });
    }

    private void addNewCode(QRcode qr_code, Player player, Map<String, Object> res, ResponseCallback responseCallback){
        codes.document(qr_code.getCodeId()).set(qr_code)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "QR code info succesuffly updated");
                        res.put("success", true);
                        res.put("Player_obj", player);
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        player.remQR(qr_code);
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
        if(!player.remQR(qr_code)){
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
                        res.put("PLayer_obj", player);
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

    public void searchByUsername(String Username, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        ArrayList<Player> data = new ArrayList<>();
        char last = Username.charAt(Username.length() - 1);
        char next = (char) (last+1);
        StringBuilder end = new StringBuilder(Username);
        end.setCharAt(Username.length() - 1, next);
        players.orderBy("userName").startAt(Username).endAt(end+"").limit(5).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i(TAG, document.getId() + " => " + document.toObject(Player.class));
                                data.add(document.toObject(Player.class));
                            }
                            res.put("success", true);
                            res.put("data", data); // type: ArrayList<Player>
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            res.put("success", false);
                            res.put("err", "" + task.getException());
                        }
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void addImage(Uri file, String codeId, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        Date date = new Date();
        long timeMilli = date.getTime();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("images/"+codeId+"/"+timeMilli+"1693.png");
        imgRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        Log.d(TAG, "Image stored successfully: " + taskSnapshot.getMetadata());
                        res.put("success", true);
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
    
    public void getRank(Player p, String field,ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        players.orderBy(field, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int i = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(p.getUserId())){
                                    res.put("rank", i);
                                }
                                Log.i(TAG, document.getData().get("userName") + " => " + document.getData().get(field));
                                usernames.add((String) document.getData().get("userName"));
                                values.add(document.getData().get(field)+"");
                                i++;
                            }
                            res.put("success", true);
                            res.put("usernames", usernames); // type: ArrayList<String>
                            res.put("values", values); // type: ArrayList<String>
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            res.put("success", false);
                            res.put("err", "" + task.getException());
                        }
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void getAllCodes(ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        codes.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<QRcode> data = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.toObject(QRcode.class));
                                data.add(document.toObject(QRcode.class));
                            }
                            res.put("success", true);
                            res.put("data", data);  // type: ArrayList<QRcode>
                            responseCallback.onResponse(res);
                        } else {
                            Exception e =task.getException();
                            Log.i(TAG, "Data could not be fetched!" + e);
                            res.put("success", false);
                            res.put("err", "" + e);
                            responseCallback.onResponse(res);
                        }
                    }
                });
    }

    public void getAllUsers(ResponseCallback responseCallback){
        // TODO: make a check to make sure user is owner

        Map<String, Object> res = new HashMap<>();
        players.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Player> data = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data.add(document.toObject(Player.class));
                            }
                            res.put("success", true);
                            res.put("data", data);  // type: ArrayList<Player>
                            responseCallback.onResponse(res);
                        } else {
                            Exception e =task.getException();
                            Log.i(TAG, "Data could not be fetched!" + e);
                            res.put("success", false);
                            res.put("err", "" + e);
                            responseCallback.onResponse(res);
                        }
                    }
                });
    }

    public void deleteCodeFromDB(String id, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        codes.document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        res.put("success", true);
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        res.put("success", false);
                        res.put("err", "" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }

    public void deletePlayerFromDB(String id, ResponseCallback responseCallback){
        Map<String, Object> res = new HashMap<>();
        players.document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        res.put("success", true);
                        responseCallback.onResponse(res);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        res.put("success", false);
                        res.put("err", "" + e);
                        responseCallback.onResponse(res);
                    }
                });
    }


}