package com.example.memegram.ui.post;

import static android.content.ContentValues.TAG;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.memegram.MainActivity;
import com.example.memegram.R;
import java.util.ArrayList;

public class TemplateGridAdapter extends RecyclerView.Adapter<com.example.memegram.ui.post.TemplateGridAdapter.TemplateGridHolder> {
    private final Context context;
    private ArrayList<Integer> ImageURLs;
    private OnTemplateClickListener mOnTemplateClickListener;

    public TemplateGridAdapter(Context context, ArrayList<Integer> ImageURL,OnTemplateClickListener onTemplateClickListener) {
        this.context = context;
        this.ImageURLs = ImageURL;
        this.mOnTemplateClickListener = onTemplateClickListener;
    }

    @NonNull
    @Override
    public com.example.memegram.ui.post.TemplateGridAdapter.TemplateGridHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_post_item,parent,false);
        return new com.example.memegram.ui.post.TemplateGridAdapter.TemplateGridHolder(view,mOnTemplateClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.memegram.ui.post.TemplateGridAdapter.TemplateGridHolder holder, int position) {
        holder.setDetails(ImageURLs.get(position));
    }

    @Override
    public int getItemCount() {
        return ImageURLs.size();
    }




    public static class TemplateGridHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView postImage;
        private final Context context;
        private OnTemplateClickListener onTemplateClickListener;
        public TemplateGridHolder(@NonNull View itemView, OnTemplateClickListener onTemplateClickListener) {
            super(itemView);
            context = itemView.getContext();
            postImage = itemView.findViewById(R.id.grid_image);
            this.onTemplateClickListener = onTemplateClickListener;
            itemView.setOnClickListener(this);
        }

        public void setDetails(int imageId) {
            postImage.setImageResource(imageId);
        }

        @Override
        public void onClick(View view) {
            onTemplateClickListener.onTemplateClick(getAdapterPosition());
        }
    }

    public interface OnTemplateClickListener{
        void onTemplateClick(int position);
    }
}