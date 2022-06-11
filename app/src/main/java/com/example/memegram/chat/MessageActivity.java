package com.example.memegram.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.memegram.LoginActivity;
import com.example.memegram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MessageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MessageListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        String user2username = getIntent().getExtras().getString("username");
        Button sendButton = findViewById(R.id.send_button);
        EditText messageBox = findViewById(R.id.message_box);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String user1username = LoginActivity.username1;
        String[] str = {user1username,user2username};
        Arrays.sort(str);
        String combinedUsername = str[0]+"-_-"+str[1];

        ArrayList<Message> messages = new  ArrayList<>();

        DatabaseReference chatsRef = database.getReference("chats").child(combinedUsername);
        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    messages.clear();
                    adapter.notifyDataSetChanged();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String messageStr = ds.child("message").getValue(String.class);
                        String senderUsername = ds.child("sender").getValue(String.class);
                        long createdAt = ds.child("time").getValue(Long.class);
                        String senderProfileURL = "";//TODO
                        messages.add(new Message(messageStr,senderUsername,createdAt,senderProfileURL));
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    // TODO: CHECK THE EXCEPTION AND FIX IT
                    //Toast.makeText(MessageActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        recyclerView = findViewById(R.id.recycler_gchat);
        adapter = new MessageListAdapter(this,messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(v -> {
            String message = messageBox.getText().toString().trim();
            if(!message.isEmpty()) {
                DatabaseReference ref = database.getReference("chats").child(combinedUsername).push();
                ref.child("message").setValue(message);
                ref.child("sender").setValue(user1username);
                Date time = new Date();
                long t = time.getTime();
                ref.child("time").setValue(t);
                messageBox.setText("");
            }
        });

    }
}