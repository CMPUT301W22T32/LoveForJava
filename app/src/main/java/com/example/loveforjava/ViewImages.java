package com.example.loveforjava;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewImages extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        //gs://loveforjava-6ab80.appspot.com/images/f5b543afd142c241a85cfa2fe708ac986f85794a68c8517b723b883bbd5b940d/16483561425651693.png
        StorageReference imgRef = storageReference.child("images/f5b543afd142c241a85cfa2fe708ac986f85794a68c8517b723b883bbd5b940d/16483561425651693.png");


        setContentView(R.layout.activity_gallery_view);
        ImageView imageView = findViewById(R.id.img);

        Glide.with(this)
                .load(imgRef)
                .into(imageView);
    }
}
