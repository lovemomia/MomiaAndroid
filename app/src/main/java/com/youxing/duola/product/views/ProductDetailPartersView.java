package com.youxing.duola.product.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/6/16.
 */
public class ProductDetailPartersView extends LinearLayout {

    public ProductDetailPartersView(Context context) {
        this(context, null);
    }

    public ProductDetailPartersView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ProductDetailPartersView create(Context context) {
        return (ProductDetailPartersView) LayoutInflater.from(context).inflate(R.layout.layout_product_detail_parters, null);
    }

}
