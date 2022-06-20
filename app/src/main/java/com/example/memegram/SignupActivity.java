package com.example.memegram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    TextView loginText;
    EditText nameText,usernameText,passwordText,confirmPasswordText,emailText;
    Button signupButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        loginText = findViewById(R.id.login_text);
        nameText = findViewById(R.id.name_text);
        usernameText = findViewById(R.id.username_text);
        passwordText = findViewById(R.id.password_text);
        confirmPasswordText = findViewById(R.id.confirm_password_text);
        signupButton = findViewById(R.id.signup_button);
        emailText = findViewById(R.id.email_text);

        loginText.setOnClickListener(view -> {
            Intent i = new Intent(SignupActivity.this,LoginActivity.class);
            startActivity(i);
        });

        signupButton.setOnClickListener(view ->{
            String name = nameText.getText().toString().trim();
            String username = usernameText.getText().toString().trim().toLowerCase();
            String password = passwordText.getText().toString().trim();
            String confirmPassword = confirmPasswordText.getText().toString().trim();
            String email = emailText.getText().toString().trim();

            if(name.isEmpty() ||
                    username.isEmpty() ||
                    password.isEmpty() ||
                    email.isEmpty() ||
                    confirmPassword.isEmpty() ){
                Toast.makeText(this,"all fields are required",Toast.LENGTH_SHORT).show();
            }else if(! password.equals(confirmPassword) ){
                Toast.makeText(this, "password and confirm password must be the same", Toast.LENGTH_SHORT).show();
            }else {
                signupButton.setEnabled(false);
                createUserAccount();
            }

        });

    }

    private void createUserAccount(){
        String name = nameText.getText().toString().trim();
        String username = usernameText.getText().toString().trim().toLowerCase();
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        // Write a user to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("users");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isTaken = false;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String str = ds.child("username").getValue(String.class).toLowerCase();
                    if (str.equals(username)) {
                        isTaken = true;
                        break;
                    }
                }

                if (isTaken) {
                    Toast.makeText(SignupActivity.this, "this username is taken", Toast.LENGTH_SHORT).show();
                    signupButton.setEnabled(true);
                } else {

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( task -> {
                        if (task.isSuccessful()) {
                            DatabaseReference myRef = database.getReference("users").push();
                            myRef.child("name").setValue(name);
                            myRef.child("username").setValue(username);
                            myRef.child("location").setValue("Addis Ababa, Ethiopia");
                            myRef.child("following").child(username).setValue(false);
                            myRef.child("followers").child(username).setValue(false);
                            myRef.child("imageURL").setValue("https://firebasestorage.googleapis.com/v0/b/memegram-696a3.appspot.com/o/profile.jpeg?alt=media&token=8c91a398-8ca0-47a2-a8bf-5272aabce1e4");
                            myRef.child("email").setValue(email).addOnCompleteListener(task1 -> {
                                signupButton.setEnabled(true);
                                Toast.makeText(SignupActivity.this, "successfully registered", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                                startActivity(intent);
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "invalid email",Toast.LENGTH_SHORT).show();
                            signupButton.setEnabled(true);
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}