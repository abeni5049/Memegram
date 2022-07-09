package com.example.memegram.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.memegram.LoginActivity;
import com.example.memegram.R;
import com.example.memegram.chat.Chat;
import com.example.memegram.ui.notifications.Notification;
import com.example.memegram.ui.notifications.NotificationsListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {

    private ArrayList<Comment> comments;
    private CommentListAdapter adapter;
    private String imageURL1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ImageView sendButton = findViewById(R.id.send);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        EditText commentEditText = findViewById(R.id.comment_edittext);
        TextView emptyView = findViewById(R.id.empty_view);

        String postKey = getIntent().getExtras().getString("postKey");
        int item_pos = getIntent().getExtras().getInt("pos");
        comments = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("posts");


        postsRef.child(postKey).child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                progressBar.setVisibility(View.VISIBLE);
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String username1 = ds.child("username").getValue(String.class);
                    String comment = ds.child("comment").getValue(String.class);

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
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    comments.add(new Comment(imageURL1,username1,comment,""));
                }
                if(comments.isEmpty()){
                    emptyView.setVisibility(View.VISIBLE);
                }else{
                    emptyView.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        RecyclerView recyclerView = findViewById(R.id.comment_list_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new CommentListAdapter(this,comments);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(view -> {
            DatabaseReference ref = postsRef.child(postKey).child("comment").push();
            ref.child("username").setValue(LoginActivity.username1);
            ref.child("comment").setValue(commentEditText.getText().toString());

            DataSnapshot ds = HomeFragment.dataSnapshots.get(item_pos);
            String username = ds.child("username").getValue(String.class);

            DatabaseReference myRef1 = database.getReference("users");

            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String uName = ds.child("username").getValue(String.class);
                        if(uName.equals(username)){
                            DatabaseReference myref = myRef1.child(ds.getKey()).child("notifications").push();
                            myref.child("postId").setValue(ds.getKey());
                            myref.child("type").setValue("comment");
                            myref.child("username").setValue(LoginActivity.username1).addOnCompleteListener(task -> {
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            commentEditText.setText("");
        });

    }
}