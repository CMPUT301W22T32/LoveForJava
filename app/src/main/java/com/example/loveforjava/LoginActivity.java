package com.example.loveforjava;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //initialize
    private EditText mEmail, musername;
    private Button loginBtn;

    //Firebase Authentication
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.useremail);
        musername = findViewById(R.id.username);
        loginBtn = findViewById(R.id.login_button);


        APIMain APIServer = new APIMain();
        /*APIServer.getPlayerInfo("3ou2WaY8MwpJbeesN0UO", new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                Log.i("login", response +"");
                Log.i("login", response.get("success")+"");
                Map<String, Object> data = (Map<String, Object>) response.get("data");
                mEmail.setText(data.get("email")+"");
            }
        });*/

        APIServer.createPlayer("Jon", "jon@jasper.j", new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                Log.i("New player success:", response.get("success")+"");
                Log.i("new id", response.get("user_id")+"");
            }
        });




        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
    }
    private void LoginUser(){
        String email = mEmail.getText().toString();
        String name = musername.getText().toString();

        //Checks if it follows the pattern of email addresses
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!name.isEmpty()){
                mAuth.createUserWithEmailAndPassword(email, name)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                musername.setError("Empty Fields Are not Allowed");
            }
        } else if (email.isEmpty()){
            mEmail.setError("Empty Fields Are not Allowed");
        } else {
            mEmail.setError("Please Enter Correct Email");
        }
    }
}