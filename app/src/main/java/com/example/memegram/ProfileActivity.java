package com.example.memegram;

import static java.util.Collections.reverse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.memegram.ui.profile.GridMemePostListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        CircleImageView profileImage = findViewById(R.id.profile_image2);
        TextView numberOfPostsText = findViewById(R.id.num_of_posts2);
        TextView numberOfFollowersText = findViewById(R.id.num_of_followers2);
        TextView numberOfFollowingText = findViewById(R.id.num_of_following2);

        String uname = getIntent().getExtras().getString("username");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("users");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("username").getValue(String.class);
                    if(name.equals(uname)){

                        Glide.with(getApplicationContext()).load( ds.child("imageURL").getValue(String.class)  ).into(profileImage);


                        int numOfFollowing = 0;
                        for (DataSnapshot ds1: ds.child("following").getChildren()){
                            if(ds1.getValue(Boolean.class)){
                                numOfFollowing += 1;
                            }
                        }
                        numberOfFollowingText.setText(String.valueOf(numOfFollowing));

                        int numOfFollowers = 0;
                        for (DataSnapshot ds1: ds.child("followers").getChildren()){
                            if(ds1.getValue(Boolean.class)){
                                numOfFollowers += 1;
                            }
                        }
                        numberOfFollowersText.setText(String.valueOf(numOfFollowers));


                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });






        ArrayList<String> ImageURLs = new ArrayList<>();
        GridMemePostListAdapter adapter = new GridMemePostListAdapter(this,ImageURLs);
        RecyclerView recyclerView = findViewById(R.id.grid_list2);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(adapter);

        DatabaseReference postsRef = database.getReference("posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            int numberOfPosts = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                ImageURLs.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String username = ds.child("username").getValue(String.class);
                    String imageURL = ds.child("imageURL").getValue(String.class);
                    int numOfLikes = 0;
                    boolean liked = false;
                    for (DataSnapshot ds1: ds.child("like").getChildren()){
                        if(ds1.getValue(Boolean.class)){
                            if (ds1.getKey().equals(uname)){
                                liked = true;
                            }
                            numOfLikes += 1;
                        }
                    }
                    if(username.equals(uname)){
                        numberOfPosts += 1;
                        ImageURLs.add(imageURL);
                    }
                }
                numberOfPostsText.setText(String.valueOf(numberOfPosts));
                reverse(ImageURLs);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}