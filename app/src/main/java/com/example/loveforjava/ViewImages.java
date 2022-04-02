package com.example.loveforjava;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.grpc.Context;

public class ViewImages extends AppCompatActivity {
    private ImageView imageView;
    private int index = 0;
    private ArrayList<StorageReference> imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_view);
        imageView = findViewById(R.id.img);
        Intent i = getIntent();
        String codeID = i.getStringExtra("code_id");

        //String codeID = "0d9074a213e6da504d34c8559e9ed06d94edfc593db2933ec09d94fbb66da7f8";

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imgList = storageReference.child("images/"+codeID);

        imgs = new ArrayList<>();

        imgList.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        Log.i("RES", listResult+"");
                        imgs.addAll(listResult.getItems());
                        if(imgs.size() != 0){
                            setListener();
                            loadImg(imgs.get(0));
                        }else{
                            Log.i("NO IMAGES", "true");
                            imageView.setImageResource(R.drawable.ic_action_texture);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("ERROR", e+"");
                    }
                });


    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener(){
        imageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                Log.i("SWIPE", "RIGHT");
                if(index > 0){
                    index--;
                    imageView.setImageResource(R.drawable.ic_action_texture);
                    loadImg(imgs.get(index));
                }
            }
            @Override
            public void onSwipeLeft() {
                Log.i("SWIPE", "LEFT");
                if(index < imgs.size()-1){
                    index++;
                    imageView.setImageResource(R.drawable.ic_action_texture);
                    loadImg(imgs.get(index));
                }
            }
        });
    }

    private void loadImg(StorageReference imgRef){
        try {
            final File localFile = File.createTempFile("Images", "bmp");
            imgRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
