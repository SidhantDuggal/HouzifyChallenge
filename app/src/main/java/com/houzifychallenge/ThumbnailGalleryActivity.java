package com.houzifychallenge;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class ThumbnailGalleryActivity extends AppCompatActivity{

    private GridView mGridView;
    private GridViewAdapter mGridAdapter;
    private int mImageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.thumbnailGalleryToolbar);
        setSupportActionBar(toolbar);

        mGridView = (GridView) findViewById(R.id.thumbnailGalleryGridView);
        mGridAdapter = new GridViewAdapter(this, R.layout.thumbnail_item_layout, getData());
        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ThumbnailItem item = (ThumbnailItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(ThumbnailGalleryActivity.this, FullScreenGalleryActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("imagePosition", String.valueOf(position));
                intent.putExtra("totalImageCount", String.valueOf(mImageCount));
                startActivity(intent);

            }

        });

    }

    private ArrayList<ThumbnailItem> getData() {

        final ArrayList<ThumbnailItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        mImageCount = imgs.length();
        for (int i = 0; i < mImageCount; i++) {

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ThumbnailItem(bitmap, "Title#" + i));

        }
        return imageItems;

    }

}
