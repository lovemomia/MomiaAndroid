package com.youxing.duola.product.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/8/7.
 */
public class ProductDetailTagsView extends LinearLayout {

    public ProductDetailTagsView(Context context) {
        this(context, null);
    }

    public ProductDetailTagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ProductDetailTagsView create(Context context) {
        return new ProductDetailTagsView(context);
    }

    public void setData(String[] tags) {
        int padding = UnitTools.dip2px(getContext(), 10);
        this.setPadding(padding, padding, padding, padding);
        for (String tag : tags) {
            TextView tv = new TextView(getContext());
            tv.setText(tag);
            tv.setTextColor(getResources().getColor(R.color.app_theme));
            tv.setTextSize(13);
            Drawable img = getResources().getDrawable(R.drawable.ic_product_header_tag);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tv.setCompoundDrawables(img, null, null, null);
            tv.setCompoundDrawablePadding(10);
            tv.setPadding(0, 0, 40, 0);
            this.addView(tv);
        }
    }
}