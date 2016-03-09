package com.youxing.duola.order.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;
import com.youxing.duola.model.Sku;
import com.youxing.duola.views.StepperView;

/**
 * Created by Jun Deng on 15/8/11.
 */
public class OrderSkuItem extends LinearLayout {

    private TextView titleTv;
    private TextView subTitleTv;
    private StepperView stepper;

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
        titleTv = (TextView) findViewById(R.id.title);
        subTitleTv = (TextView) findViewById(R.id.subTitle);
        stepper = (StepperView) findViewById(R.id.stepper);
    }

    public void setData(Sku sku, boolean isPackage) {
        String unit = isPackage ? "组" : "次";
        titleTv.setText("￥" + sku.getPrice() + "元／" + unit);

        subTitleTv.setText(sku.getDesc());
        stepper.setNumber(sku.getCount());
        if (sku.getLimit() > 0) {
            stepper.setMax(sku.getLimit());
        } else {
            stepper.setMax(Integer.MAX_VALUE);
        }
    }

    public StepperView getStepper() {
        return stepper;
    }

}
