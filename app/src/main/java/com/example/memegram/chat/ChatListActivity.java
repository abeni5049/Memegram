package com.example.memegram.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.memegram.R;
import com.example.memegram.helper.RecyclerItemClickListener;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    ArrayList<Chat> chatLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatLists = new ArrayList<>();
        for(int i = 0; i < 15; i++){
            int imageURL = getResources().getIdentifier("memegram_logo","drawable", getPackageName());
            chatLists.add(new Chat(imageURL,"Hi Dawit, how are you","abenezer_kebede"));
        }

        ChatListAdapter adapter = new ChatListAdapter(this,chatLists);
        RecyclerView recyclerView = findViewById(R.id.chat_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(ChatListActivity.this,MessageActivity.class);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }


}