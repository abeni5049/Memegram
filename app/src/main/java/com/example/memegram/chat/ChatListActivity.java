package com.example.memegram.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.memegram.LoginActivity;
import com.example.memegram.R;
import com.example.memegram.helper.RecyclerItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatListActivity extends AppCompatActivity {

    ArrayList<Chat> chatLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatLists = new ArrayList<>();

        ChatListAdapter adapter = new ChatListAdapter(this,chatLists);
        RecyclerView recyclerView = findViewById(R.id.chat_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        TextView emptyView = findViewById(R.id.empty_view);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference chatRef = database.getReference("chats");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatLists.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String[] usernames = ds.getKey().split("-_-");
                    if(usernames[0].equals(LoginActivity.username1) || usernames[1].equals(LoginActivity.username1)){
                        //TODO: change image
                        int imageURL = getResources().getIdentifier("profile","drawable", getPackageName());
                        if(usernames[0].equals(LoginActivity.username1)) {
                            chatLists.add(new Chat(imageURL,"",usernames[1]));
                        }else{
                            chatLists.add(new Chat(imageURL,"",usernames[0]));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                if(chatLists.isEmpty()){
                    emptyView.setVisibility(View.VISIBLE);
                }else{
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(ChatListActivity.this,MessageActivity.class);
                        intent.putExtra("username",chatLists.get(position).getSenderUsername());
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }


}