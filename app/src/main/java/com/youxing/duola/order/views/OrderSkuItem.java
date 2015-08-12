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
public class OrderSkuItem extends LinearLayout {

    private TextView dateTv;
    private TextView priceTv;
    private ImageView selectIv;

    public OrderSkuItem(Context context) {
        this(context, null);
    }

    public OrderSkuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static OrderSkuItem create(Context context) {
        return (OrderSkuItem) LayoutInflater.from(context).inflate(R.layout.layout_order_sku, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dateTv = (TextView) findViewById(R.id.order_sku_date);
        priceTv = (TextView) findViewById(R.id.order_sku_price);
        selectIv = (ImageView) findViewById(R.id.order_sku_select);
    }

    public void setSelect(boolean select) {
        if (select) {
            selectIv.setVisibility(View.VISIBLE);
        } else {
            selectIv.setVisibility(View.GONE);
        }
    }
}
