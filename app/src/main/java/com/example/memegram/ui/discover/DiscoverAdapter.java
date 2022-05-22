package com.example.memegram.ui.discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memegram.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder> implements Filterable {
    private List<DiscoverItem>  userList;
    private List<DiscoverItem>  userListFull;

    DiscoverAdapter(List<DiscoverItem> userList){
        this.userList = userList;
        this.userListFull = new ArrayList<>(userList);
    }
    @NonNull
    @Override
    public DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_item,parent,false);
        return new DiscoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverViewHolder holder, int position) {
        DiscoverItem currentItem = userList.get(position);
        holder.profileImage.setImageResource(currentItem.getImageURL());
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

    class DiscoverViewHolder extends  RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView usernameText,locationText;
        DiscoverViewHolder(View itemView){
            super(itemView);
            profileImage = itemView.findViewById(R.id.discover_profile_image);
            usernameText = itemView.findViewById(R.id.dicover_username);
            locationText = itemView.findViewById(R.id.discover_location);
        }
    }
}
