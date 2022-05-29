package com.example.memegram.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memegram.R;
import com.example.memegram.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private ArrayList<Notification> notifications;
    private NotificationsListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notifications = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            int imageURL = getResources().getIdentifier("memegram_logo","drawable", getContext().getPackageName());
            Notification notification = new Notification(imageURL,"abenezer_kebede","liked your meme","5h");
            notifications.add(notification);
        }

        RecyclerView recyclerView = root.findViewById(R.id.notifications_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new NotificationsListAdapter(getContext(),notifications);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}