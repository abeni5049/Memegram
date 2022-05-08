package com.example.memegram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    TextView registerText;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerText = findViewById(R.id.register_text);
        registerText.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this,SignupActivity.class);
            startActivity(i);
        });

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
        });
    }
}