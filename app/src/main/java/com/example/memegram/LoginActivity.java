package com.example.memegram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    public static  String username1="";
    public static  String profileImageURL;
    public static String mEmail;
    public static DataSnapshot userDataSnapshot;
    private FirebaseAuth mAuth;
    TextView registerText;
    Button loginButton;
    EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameText = findViewById(R.id.username_text_field);
        passwordText = findViewById(R.id.password_text_field);
        mAuth = FirebaseAuth.getInstance();
        


        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            mEmail = null;
            if(!isNetworkAvailable(this)){
                Toast.makeText(this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
            }else {
                loginButton.setEnabled(false);
                String username = usernameText.getText().toString().trim().toLowerCase();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database.getReference("users");
                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String email = ds.child("email").getValue(String.class);
                            String uName = ds.child("username").getValue(String.class).toLowerCase();
                            String imageURL = ds.child("imageURL").getValue(String.class);
                            profileImageURL = imageURL;
                            if (uName.equals(username)){
                                mEmail = email;
                                username1 = username;
                                userDataSnapshot = ds;
                                break;
                            }
                        }

                        checkAccount();

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
        
        TextView forgotPasswordText = findViewById(R.id.forgot_password_text);
        forgotPasswordText.setOnClickListener(view-> {
            if(mEmail != null) {
                mAuth.sendPasswordResetEmail(mEmail)
                        .addOnCompleteListener((OnCompleteListener<Void>) task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "reset email sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "no account with this username", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else{
                Toast.makeText(this, "no account with this username", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void checkAccount(){
        String password = passwordText.getText().toString().trim().toLowerCase();
        if(mEmail != null) {
            mAuth.signInWithEmailAndPassword(mEmail, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            boolean emailVerified = user.isEmailVerified();
                            if (emailVerified) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("username", username1);
                                startActivity(intent);
                            } else {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "email sent, please verify your email", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "incorrect username or password", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "no account with this username", Toast.LENGTH_SHORT).show();
        }

    }


}