package com.example.memegram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    TextView loginText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loginText = findViewById(R.id.login_text);
        loginText.setOnClickListener(view -> {
            Intent i = new Intent(SignupActivity.this,LoginActivity.class);
            startActivity(i);
        });
    }
}