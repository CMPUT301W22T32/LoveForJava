package com.example.loveforjava;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;

public class AfterScanActivity extends AppCompatActivity {
    Uri imageUri;
    Button btn;
    ImageView imageView;
    TextView score;
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            @SuppressLint("SdCardPath") Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/DCIM/demo.png");
                            imageView.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_AfterScan);
        getWindow().setBackgroundDrawableResource(R.drawable.qr);
        hashing();
        imageView = findViewById(R.id.iv_selected);
        btn = findViewById(R.id.btn_camera);
        Permission();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
    }
    /**
     * Open Carema
     */
    @SuppressLint("SdCardPath")
    private void openCamera() {
        //Launch Camera App
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        imageUri = Uri.fromFile(new File("/sdcard/DCIM", "demo.png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        someActivityResultLauncher.launch(intent);
    }


    public void Permission() {
        //Call Camera
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        //Dynamic Permission Request
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,//联系人的权限
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};//读写SD卡权限
            //Test if permitted
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //request permission
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }
    public static String getSHA256(String input){

        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }
    public void hashing() {

        String inputValue = "this is an example";

        // With the java libraries
        String sha256 = getSHA256(inputValue);
        score.setText(sha256);


    }


}