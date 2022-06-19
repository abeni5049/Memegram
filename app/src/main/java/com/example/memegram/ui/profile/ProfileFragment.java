package com.example.memegram.ui.profile;

import static java.util.Collections.reverse;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.memegram.EditProfileActivity;
import com.example.memegram.LoginActivity;
import com.example.memegram.R;
import com.example.memegram.databinding.FragmentProfileBinding;
import com.example.memegram.ui.home.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ArrayList<String> ImageURLs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ProgressBar progressBar = root.findViewById(R.id.progress_bar);
        Button editProfileButton = root.findViewById(R.id.edit_profile_button);
        CircleImageView profileImage = root.findViewById(R.id.profile_image);
        TextView numberOfPostsText = root.findViewById(R.id.num_of_posts);
        TextView numberOfFollowersText = root.findViewById(R.id.num_of_followers);
        TextView numberOfFollowingText = root.findViewById(R.id.num_of_following);
        Glide.with(getContext()).load(LoginActivity.profileImageURL).into(profileImage);


        ImageURLs = new ArrayList<>();
        GridMemePostListAdapter adapter = new GridMemePostListAdapter(getContext(),ImageURLs);
        RecyclerView recyclerView = root.findViewById(R.id.grid_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                            if (ds1.getKey().equals(LoginActivity.username1)){
                                liked = true;
                            }
                            numOfLikes += 1;
                        }
                    }
                    if(username.equals(LoginActivity.username1)){
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


        DatabaseReference usersRef = database.getReference("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numOfFollowing = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String uName = ds.child("username").getValue(String.class);
                    if (uName.equals(LoginActivity.username1)) {
                        for (DataSnapshot ds1: ds.child("following").getChildren()){
                            if(ds1.getValue(Boolean.class)){
                                numOfFollowing += 1;
                            }
                        }
                    }
                    numberOfFollowingText.setText(String.valueOf(numOfFollowing));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numOfFollowers = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String uName = ds.child("username").getValue(String.class);
                    if (uName.equals(LoginActivity.username1)) {
                        for (DataSnapshot ds1: ds.child("followers").getChildren()){
                            if(ds1.getValue(Boolean.class)){
                                numOfFollowers += 1;
                            }
                        }
                    }
                    numberOfFollowersText.setText(String.valueOf(numOfFollowers));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        editProfileButton.setOnClickListener(view ->{
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}