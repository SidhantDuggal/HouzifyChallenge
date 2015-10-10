package com.houzifychallenge;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import java.util.ArrayList;

public class ThumbnailGalleryActivity extends AppCompatActivity {

    private GridView mGridView;
    private GridViewAdapter mGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.thumbnailGalleryToolbar);
        setSupportActionBar(toolbar);

        mGridView = (GridView) findViewById(R.id.thumbnailGalleryGridView);
        mGridAdapter = new GridViewAdapter(this, R.layout.thumbnail_item_layout, getData());
        mGridView.setAdapter(mGridAdapter);

    }

    private ArrayList<ThumbnailItem> getData() {

        final ArrayList<ThumbnailItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ThumbnailItem(bitmap, "Image#" + i));

        }
        return imageItems;

    }

}
