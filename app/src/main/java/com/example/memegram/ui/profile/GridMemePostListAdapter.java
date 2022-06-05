package com.example.memegram.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memegram.R;

import java.util.ArrayList;

public class GridMemePostListAdapter extends RecyclerView.Adapter<GridMemePostListAdapter.GridMemePostListHolder> {
    private Context context;
    private ArrayList<String> ImageURLs;

    public GridMemePostListAdapter(Context context, ArrayList<String> ImageURL) {
        this.context = context;
        this.ImageURLs = ImageURL;
    }

    @NonNull
    @Override
    public GridMemePostListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_post_item,parent,false);
        return new GridMemePostListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridMemePostListHolder holder, int position) {
        Glide.with(context).load(ImageURLs.get(position)).into(holder.postImage);
    }

    @Override
    public int getItemCount() {
        return ImageURLs.size();
    }

    public static class GridMemePostListHolder extends RecyclerView.ViewHolder {
        private final ImageView postImage;
        public GridMemePostListHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.grid_image);
        }
    }
}