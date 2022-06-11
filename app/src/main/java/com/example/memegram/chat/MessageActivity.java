package com.example.memegram.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.memegram.R;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MessageListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ArrayList<Message> messages = new  ArrayList<>();
        for(int i = 0; i < 15 ; i++){
            messages.add(new Message("hey Abeni","abenezer_kebede",231241+i*2000,"dfa.jpg"));
        }
        recyclerView = findViewById(R.id.recycler_gchat);
        adapter = new MessageListAdapter(this,messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}