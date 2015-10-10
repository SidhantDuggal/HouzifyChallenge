package com.houzifychallenge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FullScreenGalleryActivity extends AppCompatActivity {

    private CustomViewPager mViewPager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_gallery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int position = Integer.valueOf(getIntent().getStringExtra("imagePosition"));
        setTitle("Title#"+String.valueOf(position));

        mViewPager = (CustomViewPager) findViewById(R.id.fullScreenGallery_viewPager);
        mViewPager.setAdapter(new TouchImageAdapter());
        mViewPager.setCurrentItem(position);

    }

    public class TouchImageAdapter extends PagerAdapter {

        TypedArray images;
        public TouchImageAdapter(){


            super();
            images = getResources().obtainTypedArray(R.array.image_ids);

        }

        @Override
        public int getCount() {

            return images.length();

        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            ZoomImageView img = new ZoomImageView(mContext);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images.getResourceId(position, -1));
            img.setImageBitmap(bitmap);
            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return img;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
