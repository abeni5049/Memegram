package com.example.memegram;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.memegram.R;
import com.example.memegram.ui.home.MemePostListAdapter;

import java.util.ArrayList;

public class ReportListAdapter extends RecyclerView.Adapter<com.example.memegram.ReportListAdapter.ReportListHolder> {
    private Context context;
    private ArrayList<String> ImageURLs;
    private MyClickListener listener;


    public ReportListAdapter(Context context, ArrayList<String> ImageURL, ReportListAdapter.MyClickListener listener) {
        this.context = context;
        this.ImageURLs = ImageURL;
        this.listener = listener;
    }

    @NonNull
    @Override
    public com.example.memegram.ReportListAdapter.ReportListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_list_item,parent,false);
        return new com.example.memegram.ReportListAdapter.ReportListHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.memegram.ReportListAdapter.ReportListHolder holder, int position) {
        Glide.with(context).load(ImageURLs.get(position)).into(holder.reportedImage);
    }

    @Override
    public int getItemCount() {
        return ImageURLs.size();
    }

    public static class ReportListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private  ImageView reportedImage;
        Button deleteButton,cancelButton;
        private MyClickListener listener;
        public ReportListHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);
            this.listener = listener;
            reportedImage = itemView.findViewById(R.id.repoted_image);
            deleteButton = itemView.findViewById(R.id.delete_button);
            cancelButton = itemView.findViewById(R.id.cancel_button);

            deleteButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == cancelButton.getId()){
                listener.onCancelButtonClick(this.getLayoutPosition());
            }else if(view.getId() == deleteButton.getId()){
                listener.onDeleteButtonClick(this.getLayoutPosition());
            }
        }
    }

    public interface MyClickListener {
        void onDeleteButtonClick(int pos);
        void onCancelButtonClick(int pos);
    }
}
