package com.example.memegram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class EditTemplateActivity extends AppCompatActivity {
    private final int TEXT_MAX_LENGTH = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_template);
        ImageView template_image = findViewById(R.id.template_image);
        int ImageURL = getIntent().getExtras().getInt("ImageURL");
        template_image.setImageResource(ImageURL);
        Bitmap template_image_bitmap = ((BitmapDrawable)template_image. getDrawable()). getBitmap();
        Bitmap bitmap1 = addPaddingTopForBitmap(template_image_bitmap,100);
        Bitmap bitmap2 = addPaddingBottomForBitmap(bitmap1,100);
        Bitmap bitmap3 = addPaddingLeftForBitmap(bitmap2,0);
        Bitmap bitmap4 = addPaddingRightForBitmap(bitmap3,0);
        String textTop = "my first meme I'm so excited to post this meme";
        String textBottom = "it means a lot to me";
        Bitmap bitmap5 = drawMultilineTextToBitmapTop(getApplicationContext(),bitmap4,textBottom);
        Bitmap bitmap6= drawMultilineTextToBitmapBottom(getApplicationContext(),bitmap5,textBottom);
        template_image.setImageBitmap(bitmap6);

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