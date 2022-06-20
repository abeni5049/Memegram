package com.example.memegram.ui.post;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.memegram.LoginActivity;
import com.example.memegram.R;
import com.example.memegram.databinding.FragmentPostBinding;
import com.example.memegram.helper.MemeClassifier;
import com.example.memegram.ui.profile.GridMemePostListAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class PostFragment extends Fragment implements TemplateGridAdapter.OnTemplateClickListener {

    private FragmentPostBinding binding;
    private ArrayList<Integer> ImageURLs;
    public final int PICK_IMAGE = 1;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private MemeClassifier memeClassifier;
    Bitmap pickedBitmap = null;
    private boolean isMeme;
    private FirebaseStorage storage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater,container,false);
        View root = binding.getRoot();


        memeClassifier = MemeClassifier.getInstance(getContext());
        storage = FirebaseStorage.getInstance();

        ImageURLs = new ArrayList<>();
        int res1 = getResources().getIdentifier("gallery","drawable", getContext().getPackageName());
        ImageURLs.add(res1);
        for(int i = 1; i <= 38;i++){
            int res = getResources().getIdentifier("template"+i,"drawable", getContext().getPackageName());
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
            openFilePicker();
        }else{
            Intent intent = new Intent(getContext(), EditTemplateActivity.class);
            intent.putExtra("ImageURL",ImageURLs.get(position));
            startActivity(intent);
        }
    }

    private void openFilePicker() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE);//one can be replaced with any action code

    }



    private class BackgroundProcessLocal extends AsyncTask<Bitmap, Void, String> {

        String class_label = "";
        Bitmap bitmap = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {

            bitmap = bitmaps[0];
            Bitmap reshapeBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false);
            class_label = memeClassifier.classifyFrame(reshapeBitmap);
            return class_label;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            isMeme = s.equals(MemeClassifier.IMAGE_STATUS_MEME);
            if(isMeme){
                uploadMeme();
            }else{
                Toast.makeText(getContext(), "not meme", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                pickedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                new BackgroundProcessLocal().execute(pickedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadMeme(){
        ProgressDialog progressDialog
                = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = LoginActivity.username1+"-"+formatter.format(now);
        StorageReference memesRef = storage.getReference().child("memes").child(fileName+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pickedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = memesRef.putBytes(data);

        uploadTask.addOnFailureListener(exception ->
                Toast.makeText(getContext(), "error: "+exception, Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(taskSnapshot -> {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "uploaded successfully", Toast.LENGTH_SHORT).show();
                }).addOnProgressListener(
                taskSnapshot -> {
                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int)progress + "%");
                });
        Task<Uri> urlTask = uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return memesRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                String imageURL = downloadUri.toString();
                String location = "Addis Ababa, Ethiopia";
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database.getReference("posts");

                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DatabaseReference myRef = database.getReference("posts").push();
                        myRef.child("username").setValue(LoginActivity.username1);
                        myRef.child("imageURL").setValue(imageURL);
                        myRef.child("like").child(LoginActivity.username1).setValue(false);
                        myRef.child("comment");
                        myRef.child("location").setValue(location);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}