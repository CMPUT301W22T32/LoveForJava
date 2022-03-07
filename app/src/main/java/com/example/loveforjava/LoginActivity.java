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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //initialize
    private EditText mEmail, musername;
    private Button signUpBtn;
    private APIMain APIServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        APIServer = new APIMain();

        // https://stackoverflow.com/questions/13910156/how-to-check-the-sharedpreferences-string-is-empty-or-null-android
        SharedPreferences sharedPreferences = this.getSharedPreferences("Login", MODE_PRIVATE);
        String userID = sharedPreferences.getString("USERID", null);
        if(userID != null) {
            Context context = this;
            APIServer.getPlayerInfo(userID, new ResponseCallback() {
                @Override
                public void onResponse(Map<String, Object> response) {
                    if( (Boolean) response.get("success")) {
                        Player player = (Player) response.get("Player_obj");
                        player.printPlayer();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("PLAYER", player);
                        startActivity(intent);
                    }

                }
            });

        } else {
            setContentView(R.layout.activity_login);
            mEmail = findViewById(R.id.useremail);
            musername = findViewById(R.id.username);
            signUpBtn = findViewById(R.id.login_button);

            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signUp();
                }
            });
        }
    }

    private void signUp() {
        String name = musername.getText().toString();
        String email = mEmail.getText().toString();

        // Checks if the information are all valid, gives prompt to reenter if they are not
        ArrayList<Boolean> validInfo = infoValidation(name, email);

        if(validInfo.get(0) && validInfo.get(1)) {
            APIServer.createPlayer(name, email, new ResponseCallback() {
                @Override
                public void onResponse(Map<String, Object> response) {
                    if( (Boolean) response.get("success")) {
                        String userID = (String) response.get("user_id");
                        saveUserIdLocally(userID);
                    } else
                        cannotCreateAccount();
                }
            });
        }
    }

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

    public void cannotCreateAccount() {
        Toast toast = Toast.makeText(this, "Couldn't create account", Toast.LENGTH_SHORT);
        toast.show();
    }


    private void saveUserIdLocally(String userID) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sharedPreferences.edit();
        Ed.putString("USERID", userID);
        Ed.commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USERID", userID);
        startActivity(intent);
    }
}