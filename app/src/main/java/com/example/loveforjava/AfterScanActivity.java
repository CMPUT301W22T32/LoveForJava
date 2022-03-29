package com.example.loveforjava;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;

public class AfterScanActivity extends AppCompatActivity {
    final String imgUrl = "/sdcard/DCIM/demo.png";
    private final APIMain APIServer = new APIMain();
    Uri imageUri;
    Button saveBtn;
    ImageButton camBtn;
    ImageView imageView;
    TextView score_text;
    TextView score_show;
    EditText editText;
    private Player p;
    private String hashedCode = "JEFFFFFF";
    private int score;
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
        Intent i = getIntent();
        p = (Player) i.getSerializableExtra("PLAYER");
        String rawCode = i.getStringExtra("code");
        Log.i("CODE", rawCode);
        setContentView(R.layout.activity_afterscan);

        imageView = findViewById(R.id.iv_selected);
        camBtn = findViewById(R.id.btn_camera);
        saveBtn = findViewById(R.id.save_QR);
        score_text = findViewById(R.id.score);
        Permission();
        hashedCode = hashing(rawCode);
        scoring();
        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCode();
            }
        });
    }
    /**
     * Open Camera
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

    public String hashing(String inputValue) {

        //String inputValue = "this is an example";

        // With the java libraries
        String hashed = getSHA256(inputValue);
        //score_show.setText(hashedCode);
        return hashed;
    }

    public int score_calc(String code) {
        String ints = "0123456789";
        String str;
        int score = 0;
        for(int i=0;i<code.length();i++){
            str = code.charAt(i)+"";
            if(ints.contains(str)){
                score += Integer.parseInt(str);
            }
        }
        return score;
    }

    public void scoring() {

        // With the java libraries
        //score_show=findViewById(R.id.score);
        score = score_calc(hashedCode);
        score_text.setText(score+"");
        //score_show.setText(hashedCode);


    }

    private void saveImage(){
        APIServer.addImage(imageUri, hashedCode, new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if((boolean) response.get("success")){
                    Log.i("IMG", "STORED");
                }else{
                    Log.i("IMG", "FAILED");
                }
            }
        });
    }

    private void saveCode(){
        editText = findViewById(R.id.nickname_of_QR);
        QRcode code = new QRcode(editText.getText()+"", hashedCode ,score);
        Context context = this;
        APIServer.addQRCode(code, p, new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                if( (boolean) response.get("success")){
                    p = (Player) response.get("Player_obj");
                    Intent intent = new Intent(context, QRcodeActivity.class);
                    intent.putExtra("PLAYER", p);
                    intent.putExtra("QRcode", hashedCode);
                    intent.putExtra("name", editText.getText()+"");
                    startActivity(intent);
                }else{
                    Log.i("rt", "werewerwqwewq");
                    Toast.makeText(context, "Code Already Scanned", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}