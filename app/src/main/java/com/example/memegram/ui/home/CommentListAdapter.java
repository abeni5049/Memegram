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
import com.example.memegram.ui.home.Comment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends RecyclerView.Adapter<com.example.memegram.ui.home.CommentListAdapter.CommentsListHolder>{

    private Context mContext;
    private ArrayList<Comment> Comments;

    public CommentListAdapter(Context context,ArrayList<Comment> Comments){
        mContext = context;
        this.Comments = Comments;
    }

    @NonNull
    @Override
    public com.example.memegram.ui.home.CommentListAdapter.CommentsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comments_item,parent,false);
        return new com.example.memegram.ui.home.CommentListAdapter.CommentsListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.memegram.ui.home.CommentListAdapter.CommentsListHolder holder, int position) {
        Comment comment = Comments.get(position);
        holder.CommentText.setText(comment.getCommentText());
        holder.timeText.setText(comment.getTimeText());
        holder.usernameText.setText(comment.getUsername());
        Glide.with(mContext).load( comment.getImageURL()).placeholder(R.drawable.profile).into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return Comments.size();
    }

    public class CommentsListHolder extends RecyclerView.ViewHolder{
        private CircleImageView profileImage;
        private TextView usernameText,timeText,CommentText;
        public CommentsListHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.comments_profile_image);
            usernameText = itemView.findViewById(R.id.comments_username_text);
            timeText  = itemView.findViewById(R.id.comment_time_text);
            CommentText = itemView.findViewById(R.id.comment_text);
        }
    }
}
