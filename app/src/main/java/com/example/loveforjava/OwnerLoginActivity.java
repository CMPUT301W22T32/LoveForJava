package com.example.loveforjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class OwnerLoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private EditText mEmail, musername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    public void validate() {
        mEmail = findViewById(R.id.useremail);
        musername = findViewById(R.id.username);
        String name = musername.getText().toString();
        String email = mEmail.getText().toString();
        if (name.equals("jasper")) {
            if (email.equals("jasper")){
                //Intent intent = new Intent(this, OwnerPageActivity.class);
                //startActivity(intent);
            }
        }
    }



}