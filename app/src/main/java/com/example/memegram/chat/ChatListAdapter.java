package com.example.memegram.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memegram.R;
import com.example.memegram.ui.home.MemePostListAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListHolder> {

    private Context mContext;
    private ArrayList<Chat> chatLists;

    public ChatListAdapter(Context context,ArrayList<Chat> chatLists){
        this.mContext = context;
        this.chatLists = chatLists;
    }

    @NonNull
    @Override
    public ChatListAdapter.ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_list_item,parent,false);
        return new ChatListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListHolder holder, int position) {
        Chat chat = chatLists.get(position);
        holder.profileImage.setImageResource(chat.getImageURL());
        holder.latestMessage.setText(chat.getLatestMessage());
        holder.senderUsername.setText(chat.getSenderUsername());
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public class ChatListHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView senderUsername,latestMessage;
        public ChatListHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.sender_profile_image);
            senderUsername = itemView.findViewById(R.id.sender_username_text);
            latestMessage = itemView.findViewById(R.id.latest_message);
        }
    }
}
