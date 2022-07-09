package com.example.memegram.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.memegram.LoginActivity;
import com.example.memegram.R;
import com.example.memegram.helper.RecyclerItemClickListener;
import com.example.memegram.ui.discover.DiscoverItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatListActivity extends AppCompatActivity {

    ArrayList<Chat> chatLists;
    String imageURL1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatLists = new ArrayList<>();

        ChatListAdapter adapter = new ChatListAdapter(this,chatLists);
        RecyclerView recyclerView = findViewById(R.id.chat_list_recyclerview);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        TextView emptyView = findViewById(R.id.empty_view);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference chatRef = database.getReference("chats");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                chatLists.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String[] usernames = ds.getKey().split("-_-");
                    if(usernames[0].equals(LoginActivity.username1) || usernames[1].equals(LoginActivity.username1)){
                        String username1;
                        if(usernames[0].equals(LoginActivity.username1)) {
                            username1 = usernames[1];
                        }else{
                            username1 = usernames[0];
                        }

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference usersRef = database.getReference("users");
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds : snapshot.getChildren()) {
                                    String username = ds.child("username").getValue(String.class);
                                    String imageURL = ds.child("imageURL").getValue(String.class);
                                    if (username.equals(username1)){
                                        imageURL1 = imageURL;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        chatLists.add(new Chat(imageURL1,"",username1));


                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
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