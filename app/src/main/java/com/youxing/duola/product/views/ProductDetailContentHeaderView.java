package com.youxing.duola.product.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/8/7.
 */
public class ProductDetailContentHeaderView extends LinearLayout {

    private ImageView arrowIv;
    private TextView titleTv;

    public ProductDetailContentHeaderView(Context context) {
        this(context, null);
    }

    public ProductDetailContentHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ProductDetailContentHeaderView create(Context context) {
        return (ProductDetailContentHeaderView) LayoutInflater.from(context).inflate(R.layout.layout_product_detail_content_header, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        arrowIv = (ImageView) findViewById(R.id.arrow);
        titleTv = (TextView) findViewById(R.id.title);
    }

    public void setArrowShow(boolean show) {
        arrowIv.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

}
