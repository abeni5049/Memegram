package com.example.memegram.ui.profile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.memegram.R;
import com.example.memegram.databinding.FragmentProfileBinding;
import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ArrayList<Integer> ImageURLs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        ImageURLs = new ArrayList<>();
        for(int i = 0; i < 15;i++){
            int res = getResources().getIdentifier("memegram_logo","drawable", getContext().getPackageName());
            ImageURLs.add(res);
        }
        GridMemePostListAdapter adapter = new GridMemePostListAdapter(getContext(),ImageURLs);
        RecyclerView recyclerView = root.findViewById(R.id.grid_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}