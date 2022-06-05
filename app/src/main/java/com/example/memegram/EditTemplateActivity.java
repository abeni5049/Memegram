package com.example.memegram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditTemplateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_template);
        ImageView template_image = findViewById(R.id.template_image);
        EditText topEditText = findViewById(R.id.top_edit_text);
        EditText bottomEditText = findViewById(R.id.bottom_edit_text);
        Button saveButton = findViewById(R.id.post_button);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        int ImageURL = getIntent().getExtras().getInt("ImageURL");
        template_image.setImageResource(ImageURL);

        Bitmap template_image_bitmap = ((BitmapDrawable)template_image. getDrawable()). getBitmap();
        Bitmap bitmap1 = addPaddingTopForBitmap(template_image_bitmap,100);
        Bitmap bitmap2 = addPaddingBottomForBitmap(bitmap1,100);
        Bitmap bitmap3 = addPaddingLeftForBitmap(bitmap2,0);
        Bitmap bitmap4 = addPaddingRightForBitmap(bitmap3,0);

        topEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String textTop = topEditText.getText().toString();
                String textBottom = bottomEditText.getText().toString();
                Bitmap bitmap5 = drawMultilineTextToBitmapTop(getApplicationContext(),bitmap4,textTop);
                Bitmap bitmap6= drawMultilineTextToBitmapBottom(getApplicationContext(),bitmap5,textBottom);
                template_image.setImageBitmap(bitmap6);

            }
        });

        bottomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String textTop = topEditText.getText().toString();
                String textBottom = bottomEditText.getText().toString();
                Bitmap bitmap5 = drawMultilineTextToBitmapTop(getApplicationContext(),bitmap4,textTop);
                Bitmap bitmap6= drawMultilineTextToBitmapBottom(getApplicationContext(),bitmap5,textBottom);
                template_image.setImageBitmap(bitmap6);
            }
        });

        saveButton.setOnClickListener(View->{
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String fileName = LoginActivity.username1+"-"+formatter.format(now);
            StorageReference memesRef = storage.getReference().child("memes").child(fileName+".jpg");

            // Get the data from an ImageView as bytes
            template_image.setDrawingCacheEnabled(true);
            template_image.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) template_image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = memesRef.putBytes(data);

            uploadTask.addOnFailureListener(exception ->
                    Toast.makeText(EditTemplateActivity.this, "error: "+exception, Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(taskSnapshot -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditTemplateActivity.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
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
                            myRef.child("location").setValue(location).addOnCompleteListener(task -> {
                                Toast.makeText(EditTemplateActivity.this, "yay", Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(EditTemplateActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }





    public Bitmap addPaddingTopForBitmap(Bitmap bitmap, int paddingTop) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight() + paddingTop, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, paddingTop, null);
        return outputBitmap;
    }

    public Bitmap addPaddingBottomForBitmap(Bitmap bitmap, int paddingBottom) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight() + paddingBottom, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }


    public Bitmap addPaddingRightForBitmap(Bitmap bitmap, int paddingRight) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth() + paddingRight, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }

    public Bitmap addPaddingLeftForBitmap(Bitmap bitmap, int paddingLeft) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth() + paddingLeft, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, paddingLeft, 0, null);
        return outputBitmap;
    }



    public Bitmap drawMultilineTextToBitmapTop(Context gContext,Bitmap bitmap,String gText) {
        //TODO: auto scale text when the number of line increases (to fit the white space above the meme)
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        TextPaint paint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize((int) (24 * scale));
        int textWidth = canvas.getWidth() - (int) (16 * scale);

        StaticLayout textLayout = new StaticLayout(gText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);


        float x = (bitmap.getWidth() - textWidth)/2;
        float y;
        if( textLayout.getLineCount() == 1){
            y = 25;
        }else {
            y = 0;
        }

        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();

        return bitmap;
    }

    public Bitmap drawMultilineTextToBitmapBottom(Context gContext,Bitmap bitmap,String gText) {
        //TODO: auto scale text when the number of line increases (to fit the white space above the meme)
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        TextPaint paint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize((int) (24 * scale));
        int textWidth = canvas.getWidth() - (int) (16 * scale);

        StaticLayout textLayout = new StaticLayout(gText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        float x = (bitmap.getWidth() - textWidth) / 2;
        float y;
        if( textLayout.getLineCount() == 1){
            y = bitmap.getHeight() - 75;
        }else {
            y = bitmap.getHeight() - 100;
        }
        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();

        return bitmap;
    }



}