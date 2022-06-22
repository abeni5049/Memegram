package com.example.memegram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.memegram.ui.profile.GridMemePostListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity implements ReportListAdapter.MyClickListener {

    ArrayList<String> imageURLs;
    ArrayList<String> postIDs;
    HashMap<String, Integer> reports;
    HashMap<String, ArrayList<String>> postReportsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        reports = new HashMap<>();
        imageURLs = new ArrayList<>();
        postIDs = new ArrayList<>();
        postReportsMap = new HashMap<>();
        ReportListAdapter adapter = new ReportListAdapter(this,imageURLs,this);
        RecyclerView recyclerView = findViewById(R.id.report_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reportsReference = database.getReference("reports");
        reportsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageURLs.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String  postID = ds.child("postID").getValue(String.class);
                    String username = ds.child("username").getValue(String.class);
                    String type = ds.child("type").getValue(String.class);
                    String imageURL = ds.child("imageURL").getValue(String.class);
                    if(!postReportsMap.containsKey(postID)){
                        postReportsMap.put(postID,new ArrayList<>());
                    }
                    postReportsMap.get(postID).add(ds.getKey());
                    if(reports.containsKey(postID)){
                        reports.put(postID,reports.get(postID)+1);
                    }else{
                        reports.put(postID,0);
                        imageURLs.add(imageURL);
                        postIDs.add(postID);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onDeleteButtonClick(int pos) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("posts");
        DatabaseReference reportsRef = database.getReference("reports");
        postsRef.child(postIDs.get(pos)).removeValue();
        ArrayList<String> reportsList = postReportsMap.get(postIDs.get(pos));
        for(String reportId: reportsList){
            reportsRef.child(reportId).removeValue();
        }
        Toast.makeText(this, "post deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelButtonClick(int pos) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reportsRef = database.getReference("reports");
        ArrayList<String> reportsList = postReportsMap.get(postIDs.get(pos));
        for(String reportId: reportsList){
            reportsRef.child(reportId).removeValue();
        }
        Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show();
    }
}