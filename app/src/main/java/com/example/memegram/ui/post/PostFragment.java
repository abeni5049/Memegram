package com.example.memegram.ui.post;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.memegram.EditTemplateActivity;
import com.example.memegram.R;
import com.example.memegram.databinding.FragmentPostBinding;
import com.example.memegram.ui.profile.GridMemePostListAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;


public class PostFragment extends Fragment implements TemplateGridAdapter.OnTemplateClickListener {

    private FragmentPostBinding binding;
    private ArrayList<Integer> ImageURLs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater,container,false);
        View root = binding.getRoot();


        ImageURLs = new ArrayList<>();
        for(int i = 0; i < 15;i++){
            int res = getResources().getIdentifier("sample_template2","drawable", getContext().getPackageName());
            ImageURLs.add(res);
        }
        TemplateGridAdapter adapter = new TemplateGridAdapter(getContext(),ImageURLs,this);
        RecyclerView recyclerView = root.findViewById(R.id.template_grid_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onTemplateClick(int position) {
        if(position == 0){
            final int PICK_CODE = 0;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Image from here..."),PICK_CODE);

        }else{
            Intent intent = new Intent(getContext(), EditTemplateActivity.class);
            intent.putExtra("ImageURL",ImageURLs.get(position));
            startActivity(intent);
        }
    }
}