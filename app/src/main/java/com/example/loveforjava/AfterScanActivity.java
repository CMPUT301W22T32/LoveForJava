package com.example.loveforjava;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;

public class AfterScanActivity extends AppCompatActivity {
    private final APIMain APIServer = new APIMain();
    Uri imageUri;
    Button saveBtn;
    ImageButton camBtn;
    ImageView imageView;
    TextView score_text;
    EditText editText;
    CheckBox recordLocation;
    Location location;
    private Player p;
    private String hashedCode;
    private int score;
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            @SuppressLint("SdCardPath") Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/DCIM/demo.png");
                            //Bitmap original = BitmapFactory.decodeStream(getAssets().open("1024x768.jpg"));
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


        /*  WEBSITE : http://rdcworld-android.blogspot.com
         *   SOLUTION : http://rdcworld-android.blogspot.com/2012/01/get-current-location-coordinates-city.html
         *   AUTHOR : https://draft.blogger.com/profile/09071971836590859058
         * */
        recordLocation = findViewById(R.id.record_location);
        UserLocation userLocation = new UserLocation(this);
        recordLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (displayGpsStatus()) {
                        location = userLocation.getLocation();
                    } else {
                        compoundButton.toggle();
                        alertBox("Gps Status!!", "Your GPS is: OFF");
                    }
                }
            }
        });


        imageView = findViewById(R.id.iv_selected);
        camBtn = findViewById(R.id.btn_camera);
        saveBtn = findViewById(R.id.save_QR);
        score_text = findViewById(R.id.score);
        Permission();
        hashedCode = hashCode(rawCode);
        scoreCalc(hashedCode);
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
                //saveImage();
            }
        });
    }

    private boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    protected void alertBox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_SECURITY_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
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

    public static String hashCode(String input){
        String hashedString = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            hashedString = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("HASHED", hashedString);
        return hashedString;
    }

    public int scoreCalc(String code) {
        String ints = "0123456789";
        String str;
        score = 0;
        for(int i=0;i<code.length();i++){
            str = code.charAt(i)+"";
            if(ints.contains(str)){
                score += Integer.parseInt(str);
            }
        }
        score_text.setText("Score: "+score);
        return score;
    }

    private Boolean validateFields(){
        String name = editText.getText()+"";
        if(name.isEmpty()){
            editText.setError("Empty Fields Are not Allowed");
            return false;
        }
        return true;
    }

    private void saveCode(){
        editText = findViewById(R.id.nickname_of_QR);
        if(!validateFields()){
            return;
        }
        QRcode code;
        if(location != null){
            code = new QRcode(editText.getText()+"", hashedCode ,score,
                    Double.toString(location.getLongitude()), Double.toString(location.getLatitude()));
        }else{
            code = new QRcode(editText.getText()+"", hashedCode ,score);
        }
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
                    intent.putExtra("previousActivity", "AfterScanActivity");
                    startActivity(intent);
                }else{
                    Log.i("rt", "werewerwqwewq");
                    Toast.makeText(context, "Code Already Scanned", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(imageUri != null){
            saveImage();
        }
    }

    private void saveImage(){
        signIn();
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

    private void signIn(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("AFTER", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("AFTER", "signInAnonymously:failure", task.getException());
                            //Toast.makeText(Context, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}