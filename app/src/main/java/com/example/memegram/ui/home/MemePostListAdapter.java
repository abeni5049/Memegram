package com.example.memegram.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
    private MyClickListener listener;
    public MemePostListAdapter(Context context, ArrayList<Post> posts,MyClickListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MemePostListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meme_post_item,parent,false);
        return new MemePostListHolder(view,listener);
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

    public class MemePostListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView usernameText,locationText,numberOfLikeText;
        private ImageView postImage;
        private CheckBox likeButton;
        private ImageButton commentButton;
        private MyClickListener listener;
        public MemePostListHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);
            this.listener = listener;
            usernameText = itemView.findViewById(R.id.username_text);
            locationText = itemView.findViewById(R.id.location_text);
            postImage = itemView.findViewById(R.id.meme);
            likeButton = itemView.findViewById(R.id.like_icon);
            commentButton = itemView.findViewById(R.id.comment_button);
            numberOfLikeText = itemView.findViewById(R.id.number_of_like_text);

            likeButton.setOnClickListener(this);
            commentButton.setOnClickListener(this);
            postImage.setOnClickListener(this);
        }

        public void setDetails(Post post) {
            usernameText.setText(post.getUsername());
            locationText.setText(post.getLocation());
            String likes = String.valueOf(post.getNumberOfLikes());
            if (post.getNumberOfLikes() < 2){
                likes += " like";
            }else{
                likes += " likes";
            }
            numberOfLikeText.setText(likes);
            likeButton.setChecked(post.isLiked());
            Glide.with(context).load(post.getImageURL()).into(postImage);
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == likeButton.getId()){
                if( likeButton.isChecked()) {
                    listener.onLikeButtonClick(this.getLayoutPosition());
                }else{
                    listener.onUnlikeButtonClick(this.getLayoutPosition());
                }
            }else if(view.getId() == commentButton.getId()){
                listener.OnCommentButtonClick(this.getLayoutPosition());
            }else if(view.getId() == postImage.getId()){
                listener.onDoubleTap(this.getLayoutPosition());
            }

        }
    }

    public interface MyClickListener {
        void onLikeButtonClick(int pos);
        void onUnlikeButtonClick(int pos);
        void OnCommentButtonClick(int pos);
        void onDoubleTap(int pos);
    }

}