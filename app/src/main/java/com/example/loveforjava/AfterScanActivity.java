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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;

public class AfterScanActivity extends AppCompatActivity {
    Uri imageUri;
    Button saveBtn;
    ImageButton camBtn;
    ImageView imageView;
    TextView score_text;
    EditText editText;
    CheckBox recordLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    Location location;
    private Player p;
    private String hashedCode = "JEFFFFFF";
    private int score;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
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

        /*  WEBSITE : http://rdcworld-android.blogspot.com
         *   SOLUTION : http://rdcworld-android.blogspot.com/2012/01/get-current-location-coordinates-city.html
         *   AUTHOR : https://draft.blogger.com/profile/09071971836590859058
         * */
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        recordLocation = findViewById(R.id.record_location);
        recordLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (displayGpsStatus()) {
                        locationListener = new MyLocationListener();
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                        if(locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Toast.makeText(getApplicationContext(),
                                    "Long:" + location.getLongitude() + ", Lat: " +
                                            location.getLatitude(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    alertBox("Gps Status!!", "Your GPS is: OFF");
                }
            }
        });


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

    private void saveCode(){
        editText = findViewById(R.id.nickname_of_QR);
        QRcode code = new QRcode(editText.getText()+"", hashedCode ,score,
                Double.toString(location.getLongitude()), Double.toString(location.getLatitude()));
        APIMain APIServer = new APIMain();
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