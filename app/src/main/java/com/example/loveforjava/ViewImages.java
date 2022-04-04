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

/**
 * Displays the images of the QR code
 */
public class ViewImages extends AppCompatActivity {
    private ImageView imageView;
    private int index = 0;
    private ArrayList<StorageReference> imgLinks = new ArrayList<>();
    private ArrayList<Bitmap> imgBitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_view);
        imageView = findViewById(R.id.img);
        Intent i = getIntent();
        String codeID = i.getStringExtra("code_id");

        // this code has 3 images, so it can be used for testing with multiple images
        //String codeID = "287b9da5db58dd868619d0bad1683578d7406295d71c7d9653509cb80740a05a";

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imgList = storageReference.child("images/"+codeID);

        imgList.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        Log.i("RES", listResult+"");
                        imgLinks.addAll(listResult.getItems());
                        if(imgLinks.size() != 0){
                            setListener();
                            loadImg(imgLinks.get(0));
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

    /**
     * Allow navigation between images
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setListener(){
        imageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                Log.i("SWIPE", "RIGHT");
                if(index > 0){
                    index--;
                    // set image view to loading screen so user knows image is loading
                    imageView.setImageResource(R.drawable.ic_action_texture);
                    getImage(index);
                }
            }
            @Override
            public void onSwipeLeft() {
                Log.i("SWIPE", "LEFT");
                if(index < imgLinks.size()-1){
                    index++;
                    // set image view to loading screen so user knows image is loading
                    imageView.setImageResource(R.drawable.ic_action_texture);
                    getImage(index);
                }
            }
        });
    }

    /**
     * Gets the image
     * @param i
     */
    private void getImage(int i){
        Log.i("Size", i+", "+imgBitmaps.size());
        if(i < imgBitmaps.size()){
            imageView.setImageBitmap(imgBitmaps.get(i));
        }else{
            loadImg(imgLinks.get(i));
        }
    }

    /**
     * loads the image
     * @param imgRef
     */
    private void loadImg(StorageReference imgRef){
        try {
            final File localFile = File.createTempFile("Images", "bmp");
            imgRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imgBitmaps.add(bitmap);
                    imageView.setImageBitmap(bitmap);
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
