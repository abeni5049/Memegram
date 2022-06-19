package com.example.memegram.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memegram.EditProfileActivity;
import com.example.memegram.LoginActivity;
import com.example.memegram.MainActivity;
import com.example.memegram.R;
import com.example.memegram.databinding.FragmentDiscoverBinding;
import com.example.memegram.ui.home.MemePostListAdapter;
import com.example.memegram.ui.home.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment implements DiscoverAdapter.MyClickListener {

    private FragmentDiscoverBinding binding;
    private List<DiscoverItem> userList;
    private List<DiscoverItem> userListFull;
    private DiscoverAdapter adapter;
    private ArrayList<DataSnapshot> dataSnapshots;
    private DatabaseReference usersRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDiscoverBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);
        ProgressBar progressBar = root.findViewById(R.id.progress_bar);


        dataSnapshots = new ArrayList<>();
        userList = new ArrayList<>();
        userListFull = new ArrayList<>();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                userList.clear();
                userListFull.clear();
                dataSnapshots.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String username = ds.child("username").getValue(String.class);
                    String imageURL = ds.child("imageURL").getValue(String.class);
                    String location = ds.child("location").getValue(String.class);
                    boolean isFollower = ds.child("followers").hasChild(LoginActivity.username1);
                    if( isFollower ){
                        isFollower = ds.child("followers").child(LoginActivity.username1).getValue(Boolean.class);
                    }
                    if (!username.equals(LoginActivity.username1) && !isFollower){
                        dataSnapshots.add(ds);
                        userList.add(new DiscoverItem(username,location,imageURL));
                        userListFull.add(new DiscoverItem(username,location,imageURL));
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.discover_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new DiscoverAdapter(getContext(),userList,userListFull,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return root;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public void onFollowButtonClick(int pos) {
        DataSnapshot ds1 = dataSnapshots.get(pos);
        String uname = ds1.child("username").getValue(String.class);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("users");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String uName = ds.child("username").getValue(String.class);
                    if (uName.equals(LoginActivity.username1)){
                        usersRef.child(ds.getKey()).child("following").child(uname).setValue(true).addOnCompleteListener(task -> {
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        usersRef.child(ds1.getKey()).child("followers").child(LoginActivity.username1).setValue(true).addOnCompleteListener(task -> {

        });
    }
}