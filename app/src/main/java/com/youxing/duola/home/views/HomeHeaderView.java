package com.youxing.duola.home.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.HomeModel;
import com.youxing.duola.views.PageControl;

import java.util.List;

/**
 * Created by Jun Deng on 15/6/15.
 */
public class HomeHeaderView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager pager;
    private PageControl pageControl;
    private YXNetworkImageView[] imageViews;

    private int pageCount;

//    String[] imgArray = new String[]{"http://m.chanyouji.cn/index-cover/45546-1628868.jpg",
//            "http://m.chanyouji.cn/index-cover/27926-894425.jpg",
//            "http://m.chanyouji.cn/index-cover/331-13837.jpg"};


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

    public HomeHeaderView(Context context) {
        super(context);
    }

    public HomeHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static HomeHeaderView create(Context context) {
        return (HomeHeaderView) LayoutInflater.from(context).inflate(R.layout.layout_home_header, null);
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
        pageControl = (PageControl) findViewById(R.id.pageControl);
    }

    public void setData(List<HomeModel.HomeBanner> banners) {
        //TODO

        pageCount = banners.size();
        pageControl.setNumberOfPages(pageCount);
        pageControl.setCurrentPage(0);

        imageViews = new YXNetworkImageView[pageCount];
        for (int i = 0; i < banners.size(); i++) {
            final HomeModel.HomeBanner banner = banners.get(i);
            YXNetworkImageView imageView = new YXNetworkImageView(getContext());
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageUrl(banner.getCover());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(banner.getAction())));
                }
            });
            imageViews[i] = imageView;
        }

        pager.setAdapter(new Adapter());
        pager.setOnPageChangeListener(this);

        if (pageCount > 1) {
            //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
            handler.sendMessage(handler.obtainMessage(1, (banners.size()) * 100));
        } else {
            pager.setCurrentItem((pageCount) * 100);
        }
    }

    @Override
    public void onPageSelected(int position) {
        pageControl.setCurrentPage(position % imageViews.length);
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
            return pageCount > 1 ? Integer.MAX_VALUE : 1;
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
