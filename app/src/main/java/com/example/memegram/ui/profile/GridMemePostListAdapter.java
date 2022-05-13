package com.example.memegram.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memegram.R;

import java.util.ArrayList;

public class GridMemePostListAdapter extends RecyclerView.Adapter<GridMemePostListAdapter.GridMemePostListHolder> {
    private Context context;
    private ArrayList<Integer> ImageURLs;

    public GridMemePostListAdapter(Context context, ArrayList<Integer> ImageURL) {
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
        holder.setDetails(ImageURLs.get(position));
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

        public void setDetails(int imageId) {
            postImage.setImageResource(imageId);
        }
    }
}