package com.youxing.duola.order.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;
import com.youxing.duola.model.FillOrderModel;

/**
 * Created by Jun Deng on 15/9/14.
 */
public class OrderPlaceItem extends LinearLayout {

    private TextView placeTv;
    private ImageView selectIv;

    private boolean select;

    public OrderPlaceItem(Context context) {
        this(context, null);
    }

    public OrderPlaceItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static OrderPlaceItem create(Context context) {
        return (OrderPlaceItem) LayoutInflater.from(context).inflate(R.layout.layout_order_place, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        placeTv = (TextView) findViewById(R.id.order_sku_place);
        selectIv = (ImageView) findViewById(R.id.order_sku_select);
    }

    public void setData(FillOrderModel.Place place) {
        placeTv.setText(place.getName());
    }

    public void setSelect(boolean select) {
        this.select = select;
        if (select) {
            selectIv.setVisibility(View.VISIBLE);
        } else {
            selectIv.setVisibility(View.GONE);
        }
    }

    public boolean isSelect() {
        return select;
    }
}
