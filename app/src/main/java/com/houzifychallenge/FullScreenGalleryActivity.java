package com.houzifychallenge;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class FullScreenGalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_gallery);

        String title = getIntent().getStringExtra("title");
        int position = Integer.valueOf(getIntent().getStringExtra("imagePosition"));
        int totalImageCount = Integer.valueOf(getIntent().getStringExtra("totalImageCount"));
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(position, -1));

        TextView titleTextView = (TextView) findViewById(R.id.fullscreenGallery_textView);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.fullscreenGallery_imageView);
        imageView.setImageBitmap(bitmap);

    }

}
