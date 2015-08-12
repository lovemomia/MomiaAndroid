package com.youxing.duola.order.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/8/11.
 */
public class OrderNumberItem extends LinearLayout {

    public OrderNumberItem(Context context) {
        this(context, null);
    }

    public OrderNumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static OrderNumberItem create(Context context) {
        return (OrderNumberItem) LayoutInflater.from(context).inflate(R.layout.layout_order_number, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
