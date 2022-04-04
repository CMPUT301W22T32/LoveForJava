package com.example.loveforjava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.rpc.Code;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * This is the Login Screen which is only shown once on the first time the user starts the app
 * This screen will no longer be shown after the user has logged in or signed up
 */
public class LoginActivity extends AppCompatActivity {
    //initialize
    private EditText mEmail, musername;
    private Button signUpBtn;
    private ImageButton loginBtn;
    private APIMain APIServer;
    private ImageView ownerLgn;

    /**
     * This method checks whether a user ID is saved locally
     * It launches the main screen if there is a user ID
     * It launches the login screen if there is no user ID
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        APIServer = new APIMain();
        //boolean sp = getSharedPreferences("Login", MODE_PRIVATE).edit().clear().commit();
        /*
        * WEBSITE : https://stackoverflow.com
        * SOLUTION : https://stackoverflow.com/a/13910268
        * AUTHOR : https://stackoverflow.com/users/1369222/anup-cowkur
        * */
        SharedPreferences sharedPreferences = this.getSharedPreferences("Login", MODE_PRIVATE);
        String userID = sharedPreferences.getString("USERID", null);
        if(userID != null) {
            APIServer.getPlayerInfo(userID, new ResponseCallback() {
                @Override
                public void onResponse(Map<String, Object> response) {
                    if( (Boolean) response.get("success")) {
                        Player player = (Player) response.get("Player_obj");
                        player.printPlayer();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("PLAYER", player);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
            });

        } else {
            setContentView(R.layout.activity_login);
            mEmail = findViewById(R.id.useremail);
            musername = findViewById(R.id.username);
            signUpBtn = findViewById(R.id.login_button);
            loginBtn = findViewById(R.id.qr_button);
            ownerLgn = findViewById(R.id.ownerLgnBtn);

            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signUp();
                }
            });

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login();
                }
            });

            ownerLgn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ownerLogin();
                }
            });
        }
    }

    /**
     * This method directs the admin to the admin login page
     */
    private void ownerLogin() {
        Intent intent = new Intent(this, OwnerLoginActivity.class);
        startActivity(intent);
    }

    /**
     * This method is responsible to query username and email into the database
     * It also retrieves the userID from the database and save it locally
     */
    private void signUp() {
        String name = musername.getText().toString();
        String email = mEmail.getText().toString();

        // Checks if the information are all valid, gives prompt to reenter if they are not
        ArrayList<Boolean> validInfo = infoValidation(name, email);

        // Sends only valid account information to the database
        if(validInfo.get(0) && validInfo.get(1)) {
            APIServer.createPlayer(name, email, new ResponseCallback() {
                @Override
                public void onResponse(Map<String, Object> response) {
                    if( (Boolean) response.get("success")) {
                        String userID = (String) response.get("user_id");
                        saveUserIdLocally(userID);
                    } else
                        if((Boolean) response.get("taken")){
                            Toast.makeText(LoginActivity.this, "Username "+ name+ " already taken",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            cannotCreateAccount();
                        }
                }
            });
        }
    }

    /**
     * This method directs user to the CodeScanner_activity to scan the QR code of their profile to login
     */
    public void login() {
        Intent intent = new Intent(this, CodeScanner_activity.class);
        intent.putExtra("Previous Activity", "Login");
        startActivity(intent);
    }

    /**
     * This method checks whether the username and email inputs are valid
     * It returns an ArrayList of 2 Boolean values in corresponds to the validity of the username and email
     * @param name
     * @param email
     * @return validInfo
     */
    private ArrayList<Boolean> infoValidation(String name, String email) {
        boolean validUserName = false;
        boolean validEmail = false;
        ArrayList <Boolean> validInfo = new ArrayList<Boolean>();

        // Checks if userName and email are not empty then set boolean values to true
        if(!name.isEmpty())
            validUserName = true;
        if(!email.isEmpty())
            validEmail = true;

        // Prompts error to user if userName and email are empty OR email is not a valid email address
        if(!validUserName)
            musername.setError("Empty Fields Are not Allowed");

        if(!validEmail)
            mEmail.setError("Empty Fields Are not Allowed");
        else {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                validEmail = false;
                mEmail.setError("Please Enter A Valid Email");
            }
        }

        validInfo.add(validUserName);
        validInfo.add(validEmail);

        return validInfo;
    }

    /**
     * This method will prompt an error when an account couldn't be created on the database
     */
    public void cannotCreateAccount() {
        Toast toast = Toast.makeText(this, "Couldn't create account", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * This method is responsible to save the user ID locally
     * @param userID
     */
    private void saveUserIdLocally(String userID) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sharedPreferences.edit();
        Ed.putString("USERID", userID);
        Ed.commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USERID", userID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}