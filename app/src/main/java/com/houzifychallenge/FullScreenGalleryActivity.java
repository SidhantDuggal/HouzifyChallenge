package com.houzifychallenge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FullScreenGalleryActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ImageView mImageView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_gallery);

        String title = getIntent().getStringExtra("title");
        int position = Integer.valueOf(getIntent().getStringExtra("imagePosition"));
        int totalImageCount = Integer.valueOf(getIntent().getStringExtra("totalImageCount"));
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(position, -1));

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TouchImageAdapter());
        mViewPager.setCurrentItem(position);

//        mImageView = (ImageView) findViewById(R.id.fullscreenGallery_imageView);
//        mImageView.setImageBitmap(bitmap);

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

            TouchImageView img = new TouchImageView(mContext);
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

}
