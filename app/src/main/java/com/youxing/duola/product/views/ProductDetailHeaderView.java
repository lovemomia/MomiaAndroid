package com.youxing.duola.product.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/6/16.
 */
public class ProductDetailHeaderView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private ViewPager pager;
    private YXNetworkImageView[] imageViews;

    String[] imgArray = new String[]{"http://m.chanyouji.cn/index-cover/45546-1628868.jpg",
            "http://m.chanyouji.cn/index-cover/27926-894425.jpg",
            "http://m.chanyouji.cn/index-cover/331-13837.jpg"};

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int index = (Integer) msg.obj;
                    pager.setCurrentItem(index);
                    sendMessageDelayed(obtainMessage(1, (index + 1)), 3000);
                    break;
            }
        }
    };

    public ProductDetailHeaderView(Context context) {
        this(context, null);
    }

    public ProductDetailHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ProductDetailHeaderView create(Context context) {
        return (ProductDetailHeaderView) LayoutInflater.from(context).inflate(R.layout.layout_product_detail_header, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeMessages(1);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        pager = (ViewPager) findViewById(R.id.pager);
    }

    public void setData() {
        //TODO


        imageViews = new YXNetworkImageView[imgArray.length];
        for (int i = 0; i < imgArray.length; i++) {
            YXNetworkImageView imageView = new YXNetworkImageView(getContext());
            imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageUrl(imgArray[i]);
            imageViews[i] = imageView;
        }

        pager.setAdapter(new Adapter());
        pager.setOnPageChangeListener(this);

        if (imgArray.length > 1) {
            //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
            handler.sendMessage(handler.obtainMessage(1, (imgArray.length) * 100));
        } else {
            pager.setCurrentItem((imgArray.length) * 100);
        }
    }

    @Override
    public void onPageSelected(int position) {
        //TODO
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            handler.removeMessages(1);

        } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
            handler.sendMessageDelayed(handler.obtainMessage(1, pager.getCurrentItem() + 1), 3000);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgArray.length > 1 ? Integer.MAX_VALUE : 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            YXNetworkImageView view = imageViews[position % imageViews.length];
            container.removeView(view);
            container.addView(view, 0);
            return view;
        }
    }

}
