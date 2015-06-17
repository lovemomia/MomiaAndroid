package com.youxing.duola.product.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/6/17.
 */
public class ProductDetailInfoView extends LinearLayout {

    private ImageView iconIv;
    private TextView titleTv;

    public ProductDetailInfoView(Context context) {
        this(context, null);
    }

    public ProductDetailInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ProductDetailInfoView create(Context context) {
        return (ProductDetailInfoView) LayoutInflater.from(context).inflate(R.layout.layout_product_detail_info, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (ImageView) findViewById(R.id.icon);
        titleTv = (TextView) findViewById(R.id.title);
    }

    public void setIcon(int resId) {
        iconIv.setImageResource(resId);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

}
