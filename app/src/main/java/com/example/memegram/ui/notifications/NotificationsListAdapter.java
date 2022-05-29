package com.example.memegram.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memegram.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationsListHolder>{

    private Context mContext;
    private ArrayList<Notification> notifications;

    public NotificationsListAdapter(Context context,ArrayList<Notification> notifications){
        mContext = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notifications_item,parent,false);
        return new NotificationsListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsListHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.notificationText.setText(notification.getNotificationText());
        holder.timeText.setText(notification.getTimeText());
        holder.usernameText.setText(notification.getUsername());
        holder.profileImage.setImageResource(notification.getImageURL());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationsListHolder extends RecyclerView.ViewHolder{
        private CircleImageView profileImage;
        private TextView usernameText,timeText,notificationText;
        public NotificationsListHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.notifications_profile_image);
            usernameText = itemView.findViewById(R.id.notifications_username_text);
            timeText  = itemView.findViewById(R.id.notification_time_text);
            notificationText = itemView.findViewById(R.id.notification_text);
        }
    }
}
