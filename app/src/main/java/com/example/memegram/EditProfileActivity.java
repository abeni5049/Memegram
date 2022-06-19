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

import java.util.Arrays;
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
                            String oldUsername = LoginActivity.username1;
                            if(!oldUsername.equals(newUsername)) {
                                LoginActivity.username1 = newUsername;

                                //update posts
                                DatabaseReference postsRef = database.getReference("posts");
                                postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds : snapshot.getChildren()) {
                                            String username = ds.child("username").getValue(String.class);
                                            if(username.equals(oldUsername)){
                                                postsRef.child(ds.getKey()).child("username").setValue(newUsername);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                //update following and followers
                                DatabaseReference userRef = database.getReference("users");
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds: snapshot.getChildren()){
                                            boolean hasChild = ds.child("followers").hasChild(oldUsername);
                                            if(hasChild){
                                                boolean isTrue = ds.child("followers").child(oldUsername).getValue(Boolean.class);
                                                userRef.child(ds.getKey()).child("followers").child(newUsername).setValue(isTrue);
                                                userRef.child(ds.getKey()).child("followers").child(oldUsername).removeValue();
                                            }

                                            boolean hasChild1 = ds.child("following").hasChild(oldUsername);
                                            if(hasChild1){
                                                boolean isTrue = ds.child("following").child(oldUsername).getValue(Boolean.class);
                                                userRef.child(ds.getKey()).child("following").child(newUsername).setValue(isTrue);
                                                userRef.child(ds.getKey()).child("following").child(oldUsername).removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}

                                });

                                //update notifications
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds: snapshot.getChildren()){
                                            userRef.child(ds.getKey()).child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot ds1: snapshot.getChildren()){
                                                        if(ds1.child("username").getValue(String.class).equals(oldUsername)){
                                                            userRef.child(ds.getKey()).child("notifications").child(ds1.getKey()).child("username").setValue(newUsername);
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {}
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}
                                });

                                //update like
                                postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds: snapshot.getChildren()){
                                            boolean hasChild =  ds.child("like").hasChild(oldUsername);
                                            if(hasChild){
                                                boolean isTrue = ds.child("like").child(oldUsername).getValue(Boolean.class);
                                                postsRef.child(ds.getKey()).child("like").child(newUsername).setValue(isTrue);
                                                postsRef.child(ds.getKey()).child("like").child(oldUsername).removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}
                                });

                                //update comment
                                postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds: snapshot.getChildren()){
                                            postsRef.child(ds.getKey()).child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot ds1: snapshot.getChildren()){
                                                        if(ds1.child("username").getValue(String.class).equals(oldUsername)){
                                                            postsRef.child(ds.getKey()).child("comment").child(ds1.getKey()).child("username").setValue(newUsername);
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {}
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}
                                });

                                //update chat
                                DatabaseReference chatRef = database.getReference("chats");
                                chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds: snapshot.getChildren()){
                                            String[] usernames = ds.getKey().split("-_-");
                                            if(usernames[0].equals(oldUsername) || usernames[1].equals(oldUsername)){
                                                if(usernames[0].equals(oldUsername)) {
                                                    usernames[0] = newUsername;
                                                }else{
                                                    usernames[1] = newUsername;
                                                }
                                                Arrays.sort(usernames);
                                                for(DataSnapshot ds1: ds.getChildren()){
                                                    String message = ds1.child("message").getValue(String.class);
                                                    String sender = ds1.child("sender").getValue(String.class);
                                                    if(sender.equals(oldUsername)){
                                                        sender = newUsername;
                                                    }
                                                    long time  = ds1.child("time").getValue(Long.class);
                                                    chatRef.child(usernames[0]+"-_-"+usernames[1]).child(ds1.getKey()).child("message").setValue(message);
                                                    chatRef.child(usernames[0]+"-_-"+usernames[1]).child(ds1.getKey()).child("sender").setValue(sender);
                                                    chatRef.child(usernames[0]+"-_-"+usernames[1]).child(ds1.getKey()).child("time").setValue(time);
                                                    chatRef.child(ds.getKey()).removeValue();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }

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