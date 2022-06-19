package com.example.memegram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    public static  String username1=" ";
    public static  String profileImageURL;
    public static DataSnapshot userDataSnapshot;
    TextView registerText;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameText = findViewById(R.id.username_text_field);
        EditText passwordText = findViewById(R.id.password_text_field);


        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            if(!isNetworkAvailable(this)){
                Toast.makeText(this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
            }else {
                loginButton.setEnabled(false);
                String username = usernameText.getText().toString().trim().toLowerCase();
                String password = passwordText.getText().toString().trim();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database.getReference("users");
                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isCorrect = false;
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String uName = ds.child("username").getValue(String.class).toLowerCase();
                            String pass = ds.child("password").getValue(String.class);
                            String imageURL = ds.child("imageURL").getValue(String.class);
                            profileImageURL = imageURL;
                            if(uName != null && pass != null)
                                if (uName.equals(username) && pass.equals(password)) {
                                    isCorrect = true;
                                    userDataSnapshot = ds;
                                    break;
                                }
                        }

                        if(isCorrect) {
                            username1 = username;
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "incorrect username or password", Toast.LENGTH_SHORT).show();
                        }
                        loginButton.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "error occurred , please check your internet connection", Toast.LENGTH_SHORT).show();
                        loginButton.setEnabled(true);
                    }
                });
            }

        });

        registerText = findViewById(R.id.register_text);
        registerText.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this,SignupActivity.class);
            startActivity(i);
        });

    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}