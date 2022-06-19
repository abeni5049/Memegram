package com.example.memegram.ui.home;

import static java.util.Collections.reverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memegram.EditTemplateActivity;
import com.example.memegram.LoginActivity;
import com.example.memegram.ProfileActivity;
import com.example.memegram.R;
import com.example.memegram.chat.ChatListActivity;
import com.example.memegram.chat.MessageActivity;
import com.example.memegram.databinding.FragmentHomeBinding;
import com.example.memegram.helper.RecyclerItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements MemePostListAdapter.MyClickListener {

    private FragmentHomeBinding binding;
    private ArrayList<Post> posts;
    public  static ArrayList<DataSnapshot> dataSnapshots;
    private DatabaseReference postsRef;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        posts = new ArrayList<>();
        dataSnapshots = new ArrayList<>();


        ProgressBar progressBar = root.findViewById(R.id.progress_bar);

        MemePostListAdapter adapter = new MemePostListAdapter(getContext(),posts,this);
        RecyclerView recyclerView = root.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        postsRef = database.getReference("posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                posts.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    dataSnapshots.add(ds);
                    String username = ds.child("username").getValue(String.class);

                    // get user profile image
                    String profileURL= "https://firebasestorage.googleapis.com/v0/b/memegram-696a3.appspot.com/o/profile.jpeg?alt=media&token=8c91a398-8ca0-47a2-a8bf-5272aabce1e4";
//                    DatabaseReference usersRef = database.getReference("users");
//                    for (DataSnapshot ds2 : ) {
//                        String uName = ds2.child("username").getValue(String.class);
//                        if (username.equals(uName)) {
//                            profileURL = ds.child("imageURL").getValue(String.class);
//                        }
//                    }
                    //

                    String imageURL = ds.child("imageURL").getValue(String.class);
                    String location = ds.child("location").getValue(String.class);
                    int numOfLikes = 0;
                    boolean liked = false;
                    for (DataSnapshot ds1: ds.child("like").getChildren()){
                        if(ds1.getValue(Boolean.class)){
                            if (ds1.getKey().equals(LoginActivity.username1)){
                                liked = true;
                            }
                            numOfLikes += 1;
                        }
                    }
                    posts.add(new Post(username,location,imageURL,numOfLikes,liked, profileURL));
                }
                reverse(posts);
                reverse(dataSnapshots);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.chat_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_chat) {
            Intent intent = new Intent(getContext(),ChatListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLikeButtonClick(int pos) {
        DataSnapshot ds = dataSnapshots.get(pos);
        postsRef.child(ds.getKey()).child("like").child(LoginActivity.username1).setValue(true).addOnCompleteListener(task -> {
            String username = ds.child("username").getValue(String.class);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef1 = database.getReference("users");

            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String uName = ds.child("username").getValue(String.class);
                        if(uName.equals(username)){
                            DatabaseReference myref = myRef1.child(ds.getKey()).child("notifications").push();
                            myref.child("postId").setValue(ds.getKey());
                            myref.child("type").setValue("like");
                            myref.child("username").setValue(LoginActivity.username1).addOnCompleteListener(task -> {
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

    }

    @Override
    public void onUnlikeButtonClick(int pos) {
        DataSnapshot ds = dataSnapshots.get(pos);
        postsRef.child(ds.getKey()).child("like").child(LoginActivity.username1).setValue(false).addOnCompleteListener(task -> {
        });
    }

    @Override
    public void onCommentButtonClick(int pos) {
        DataSnapshot ds = dataSnapshots.get(pos);
        Intent intent = new Intent(getContext(),CommentActivity.class);
        intent.putExtra("postKey",ds.getKey());
        intent.putExtra("pos",pos);
        startActivity(intent);
    }

    @Override
    public void onUsernameTextClick(int pos) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("username",posts.get(pos).getUsername());
        startActivity(intent);
    }

    @Override
    public void onDoubleTap(int pos) {
        
    }
}