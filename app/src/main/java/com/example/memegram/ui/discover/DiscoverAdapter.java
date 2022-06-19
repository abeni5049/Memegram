package com.example.memegram.ui.discover;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memegram.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder> implements Filterable {
    private List<DiscoverItem>  userList;
    private List<DiscoverItem>  userListFull;
    private Context context;
    private MyClickListener listener;

    DiscoverAdapter(Context context,List<DiscoverItem> userList,List<DiscoverItem> userListFull,MyClickListener listener){
        this.userList = userList;
        this.userListFull = userListFull;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_item,parent,false);
        return new DiscoverViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverViewHolder holder, int position) {
        DiscoverItem currentItem = userList.get(position);
        Glide.with(context).load(currentItem.getImageURL()).into(holder.profileImage);
        holder.usernameText.setText(currentItem.getUsername());
        holder.locationText.setText(currentItem.getLocation());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DiscoverItem> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0 ){
                filteredList.addAll(userListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (DiscoverItem item : userListFull){
                    if(item.getUsername().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            userList.clear();
            userList.addAll( (List) filterResults.values );
            notifyDataSetChanged();
        }
    };

    class DiscoverViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{
        ImageView profileImage;
        TextView usernameText,locationText;
        Button followButton;
        DiscoverViewHolder(View itemView,MyClickListener listener){
            super(itemView);
            profileImage = itemView.findViewById(R.id.discover_profile_image);
            usernameText = itemView.findViewById(R.id.dicover_username);
            locationText = itemView.findViewById(R.id.discover_location);
            followButton = itemView.findViewById(R.id.discover_follow_button);
            followButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == followButton.getId()){
                followButton.setText("following");
                followButton.setTextColor(Color.BLACK);
                followButton.setBackgroundColor(Color.WHITE);
                listener.onFollowButtonClick(this.getLayoutPosition());
            }
        }

    }

    public interface MyClickListener {
        void onFollowButtonClick(int pos);
    }

}
