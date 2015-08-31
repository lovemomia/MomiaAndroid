package com.youxing.duola.product.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youxing.common.adapter.RecyclingPagerAdapter;
import com.youxing.common.app.Enviroment;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Product;
import com.youxing.duola.utils.PriceUtils;

import java.util.List;

/**
 * Created by Jun Deng on 15/6/16.
 */
public class ProductDetailHeaderView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private ViewPager pager;

    private TextView titleTv;
    private TextView numberTv;
    private TextView priceTv;
    private TextView pageNumTv;

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
        int width = Enviroment.screenWidth(getContext());
        pager.setLayoutParams(new RelativeLayout.LayoutParams(width, width * 3/4));
        titleTv = (TextView) findViewById(R.id.title);
        numberTv = (TextView) findViewById(R.id.number);
        priceTv = (TextView) findViewById(R.id.price);
        pageNumTv = (TextView) findViewById(R.id.page_num);
    }

    public void setData(Product product) {
        titleTv.setText(product.getTitle());
        if (product.getJoined() > 0) {
            numberTv.setText(product.getJoined() + "人已报名");
            numberTv.setVisibility(View.VISIBLE);

        } else {
            numberTv.setVisibility(View.GONE);
        }
        priceTv.setText(PriceUtils.formatPriceString(product.getPrice()));

        pageCount = product.getImgs().size();
        ImagePagerAdapter adapter = new ImagePagerAdapter(getContext(), product.getImgs());
        if (pageCount > 1) {
            adapter.setInfiniteLoop(true);
        }
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
        pager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % pageCount);

        if (pageCount <= 1) {
            pageNumTv.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    public void onPageSelected(int position) {
        int current = position % pageCount + 1;
        String text = current + "/" + pageCount;

        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.indexOf("/"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_image)),
                text.indexOf("/"), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        pageNumTv.setText(ss);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    public class ImagePagerAdapter extends RecyclingPagerAdapter {

        private Context context;
        private List<String> imgs;

        private int           size;
        private boolean       isInfiniteLoop;

        public ImagePagerAdapter(Context context, List<String> imgs) {
            this.context = context;
            this.imgs = imgs;
            this.size = pageCount;
            isInfiniteLoop = false;
        }

        @Override
        public int getCount() {
            // Infinite loop
            return isInfiniteLoop ? Integer.MAX_VALUE : pageCount;
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
                YXNetworkImageView imageView = new YXNetworkImageView(getContext());
                imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setDefaultImageResId(R.drawable.bg_default_image);
                view = holder.imageView = imageView;
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            holder.imageView.setImageUrl(imgs.get(getPosition(position)));
            return view;
        }

        private class ViewHolder {
            YXNetworkImageView imageView;
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
