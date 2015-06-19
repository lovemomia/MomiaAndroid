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
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Product;

/**
 * Created by Jun Deng on 15/6/16.
 */
public class ProductDetailHeaderView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private ViewPager pager;
    private YXNetworkImageView[] imageViews;

    private TextView titleTv;
    private TextView numberTv;
    private TextView priceTv;

    private int pageCount;

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
    protected void onFinishInflate() {
        super.onFinishInflate();
        pager = (ViewPager) findViewById(R.id.pager);
        titleTv = (TextView) findViewById(R.id.title);
        numberTv = (TextView) findViewById(R.id.number);
        priceTv = (TextView) findViewById(R.id.price);
    }

    public void setData(Product product) {
        titleTv.setText(product.getTitle());
        numberTv.setText(product.getJoined() + "人报名");
        priceTv.setText("￥" + product.getPrice());

        pageCount = product.getImgs().size();

        imageViews = new YXNetworkImageView[pageCount];
        for (int i = 0; i < pageCount; i++) {
            String url = product.getImgs().get(i);
            YXNetworkImageView imageView = new YXNetworkImageView(getContext());
            imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageUrl(url);
            imageViews[i] = imageView;
        }

        pager.setAdapter(new Adapter());
        pager.setOnPageChangeListener(this);

        pager.setCurrentItem(pageCount * 100);
    }

    @Override
    public void onPageSelected(int position) {
        //TODO
    }

    @Override
    public void onPageScrollStateChanged(int state) {
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
