package com.example.memegram.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memegram.R;

import java.util.ArrayList;

public class MemePostListAdapter extends RecyclerView.Adapter<MemePostListAdapter.MemePostListHolder> {
    private Context context;
    private ArrayList<Post> posts;

    public MemePostListAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public MemePostListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meme_post_item,parent,false);
        return new MemePostListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemePostListHolder holder, int position) {
        Post post = posts.get(position);
        holder.setDetails(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MemePostListHolder extends RecyclerView.ViewHolder {
        private TextView usernameText,locationText;
        private ImageView postImage;
        public MemePostListHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            locationText = itemView.findViewById(R.id.location_text);
            postImage = itemView.findViewById(R.id.meme);
        }

        public void setDetails(Post post) {
            usernameText.setText(post.getUsername());
            locationText.setText(post.getLocation());
            Glide.with(context).load(post.getImageURL()).into(postImage);
        }
    }
}