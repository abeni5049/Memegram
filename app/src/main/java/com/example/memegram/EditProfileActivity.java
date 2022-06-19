package com.example.memegram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.memegram.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        EditText nameEditText = findViewById(R.id.name_text);
        EditText usernameEditText = findViewById(R.id.username_text);
        EditText passwordEditText = findViewById(R.id.password_text);
        EditText confirmPasswordEditText = findViewById(R.id.confirm_password_text);
        CircleImageView profileImage = findViewById(R.id.profile_image);
        TextView changeProfileImageText = findViewById(R.id.change_profile_image_text);
        Button updateButton = findViewById(R.id.update_button);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("users");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String uName = ds.child("username").getValue(String.class);
                    if(uName.equals(LoginActivity.username1)) {
                        String name = ds.child("name").getValue(String.class);
                        String username = ds.child("username").getValue(String.class);
                        String password = ds.child("password").getValue(String.class);
                        String ProfileImageURL = ds.child("imageURL").getValue(String.class);

                        usernameEditText.setText(username);
                        nameEditText.setText(name);
                        passwordEditText.setText(password);
                        confirmPasswordEditText.setText(password);
                        Glide.with(getApplicationContext()).load(ProfileImageURL).into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        updateButton.setOnClickListener(view ->{
            String name = nameEditText.getText().toString().trim();
            String newUsername = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String  confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if(name.isEmpty() ||
                    newUsername.isEmpty() ||
                    password.isEmpty() ||
                    confirmPassword.isEmpty() ){
                Toast.makeText(this,"all fields are required",Toast.LENGTH_SHORT).show();
            }else if(! password.equals(confirmPassword) ) {
                Toast.makeText(this, "password and confirm password must be the same", Toast.LENGTH_SHORT).show();
            }else {

                DatabaseReference myRef = database.getReference("users");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isTaken = false;
                        String currentUseSnapshot = "unknown";
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String uName = ds.child("username").getValue(String.class);
                            if (uName != null)
                                if (uName.equals(LoginActivity.username1)) {
                                    currentUseSnapshot = ds.getKey();
                                    if (uName.equals(newUsername) && !(newUsername.equals(LoginActivity.username1))) {
                                        isTaken = true;
                                        break;
                                    }
                                }
                        }

                        if (isTaken) {
                            Toast.makeText(EditProfileActivity.this, "this username is taken", Toast.LENGTH_SHORT).show();
                        } else if (currentUseSnapshot != null) {
                            DatabaseReference myRef = database.getReference("users").child(currentUseSnapshot);
                            myRef.child("name").setValue(name);
                            myRef.child("username").setValue(newUsername);
                            LoginActivity.username1 = newUsername;
                            myRef.child("password").setValue(password).addOnCompleteListener(task ->
                                    Toast.makeText(EditProfileActivity.this, "successfully updated", Toast.LENGTH_SHORT).show());


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
}