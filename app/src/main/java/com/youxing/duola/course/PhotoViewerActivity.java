package com.youxing.duola.course;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.youxing.common.adapter.RecyclingPagerAdapter;
import com.youxing.common.views.ZoomableImageView;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;

/**
 * Created by Jun Deng on 16/3/16.
 */
public class PhotoViewerActivity extends SGActivity implements ViewPager.OnPageChangeListener {

    private String[] urls;

    private ViewPager viewPager;
    private ImagePagerAdapter adapter;
    private TextView positionTv;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        urls = getIntent().getStringArrayExtra("urls");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back_white);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);
        adapter = new ImagePagerAdapter(this, urls).setInfiniteLoop(false);
        viewPager.setAdapter(adapter);
        positionTv = (TextView) findViewById(R.id.position);
        positionTv.setText("1/" + urls.length);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        positionTv.setText((adapter.getPosition(position) + 1) + "/" + urls.length);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public class ImagePagerAdapter extends RecyclingPagerAdapter {

        private Context context;
        private String[] urls;

        private int           size;
        private boolean       isInfiniteLoop;

        public ImagePagerAdapter(Context context, String[] urls) {
            this.context = context;
            this.urls = urls;
            this.size = urls.length;
            isInfiniteLoop = false;
        }

        @Override
        public int getCount() {
            // Infinite loop
            return isInfiniteLoop ? Integer.MAX_VALUE : size;
        }

        /**
         * get really position
         *
         * @param position
         * @return
         */
        private int getPosition(int position) {
            return isInfiniteLoop ? position % size : position;
        }

        @Override
        public View getView(int position, View view, ViewGroup container) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                ZoomableImageView imageView = new ZoomableImageView(PhotoViewerActivity.this);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                imageView.setDefaultImageResId(R.drawable.bg_default_image);
                view = holder.imageView = imageView;
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            holder.imageView.setImageUrl(urls[getPosition(position)]);
            return view;
        }

        private class ViewHolder {
            ZoomableImageView imageView;
        }

        /**
         * @return the isInfiniteLoop
         */
        public boolean isInfiniteLoop() {
            return isInfiniteLoop;
        }

        /**
         * @param isInfiniteLoop the isInfiniteLoop to set
         */
        public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
            this.isInfiniteLoop = isInfiniteLoop;
            return this;
        }
    }
}
