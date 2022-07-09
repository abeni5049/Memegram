package com.example.memegram;



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
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.memegram.ui.discover.DiscoverAdapter;
import com.example.memegram.ui.discover.DiscoverItem;
import com.example.memegram.ui.home.MemePostListAdapter;
import com.example.memegram.ui.home.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FollowersActivity extends AppCompatActivity implements DiscoverAdapter.MyClickListener {

    private List<DiscoverItem> userList;
    private List<DiscoverItem> userListFull;
    private DiscoverAdapter adapter;
    private ArrayList<DataSnapshot> dataSnapshots;
    private DatabaseReference usersRef;
    private HashSet<String> following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        TextView emptyView = findViewById(R.id.empty_view);


        setTitle("followers");

        dataSnapshots = new ArrayList<>();
        userList = new ArrayList<>();
        userListFull = new ArrayList<>();
        following = new HashSet<>();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    boolean isFollower = ds.child("following").hasChild(LoginActivity.username1);
                    if( isFollower ){
                        isFollower = ds.child("following").child(LoginActivity.username1).getValue(Boolean.class);
                    }
                    if (!username.equals(LoginActivity.username1) && isFollower){
                        dataSnapshots.add(ds);
                        userList.add(new DiscoverItem(username,location,imageURL));
                        userListFull.add(new DiscoverItem(username,location,imageURL));
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
                if(userListFull.isEmpty()){
                    emptyView.setVisibility(View.VISIBLE);
                }else{
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerView recyclerView = findViewById(R.id.discover_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new DiscoverAdapter(this,userList,userListFull,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }






    @Override
    public void onFollowButtonClick(int pos) {
//        DataSnapshot ds1 = dataSnapshots.get(pos);
//        String uname = ds1.child("username").getValue(String.class);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef1 = database.getReference("users");
//        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    String uName = ds.child("username").getValue(String.class);
//                    if (uName.equals(LoginActivity.username1)){
//                        usersRef.child(ds.getKey()).child("following").child(uname).setValue(true).addOnCompleteListener(task -> {
//                        });
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//        usersRef.child(ds1.getKey()).child("followers").child(LoginActivity.username1).setValue(true).addOnCompleteListener(task -> {
//
//        });
    }

}