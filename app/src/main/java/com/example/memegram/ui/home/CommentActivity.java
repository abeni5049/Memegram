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

import com.example.memegram.LoginActivity;
import com.example.memegram.R;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ImageView sendButton = findViewById(R.id.send);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        EditText commentEditText = findViewById(R.id.comment_edittext);

        String postKey = getIntent().getExtras().getString("postKey");
        int item_pos = getIntent().getExtras().getInt("pos");
        comments = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("posts");

        int imageURL = this.getResources().getIdentifier("memegram_logo","drawable", this.getPackageName());

        postsRef.child(postKey).child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                comments.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String username = ds.child("username").getValue(String.class);
                    String comment = ds.child("comment").getValue(String.class);
                    comments.add(new Comment(imageURL,username,comment,"5h"));
                    adapter.notifyDataSetChanged();
                }
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