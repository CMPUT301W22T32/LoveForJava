package com.example.loveforjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //initialize
    EditText name, email;
    Button login_button;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Typecasting
        name = findViewById(R.id.username);
        email = findViewById(R.id.useremail);
        login_button = findViewById(R.id.login_button);

        //Firebase instance
        auth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailID, userName;
                userName = name.getText().toString();
                emailID = email.getText().toString();

                if (userName.isEmpty()){
                    name.setError("Please Enter Your Username");
                    name.requestFocus();
                } else if (emailID.isEmpty()) {
                    name.setError("Please Enter Your Email");
                    name.requestFocus();
                } else {
                    auth.createUserWithEmailAndPassword(userName, emailID).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        });
    }
}
