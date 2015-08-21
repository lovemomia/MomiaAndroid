package com.youxing.duola.order.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;
import com.youxing.duola.model.Sku;
import com.youxing.duola.utils.PriceUtils;
import com.youxing.duola.views.StepperView;

import org.w3c.dom.Text;

/**
 * Created by Jun Deng on 15/8/11.
 */
public class OrderNumberItem extends LinearLayout {

    private TextView priceTv;
    private StepperView stepperView;

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
        priceTv = (TextView) findViewById(R.id.price);
        stepperView = (StepperView) findViewById(R.id.stepper);
    }

    public StepperView getStepperView() {
        return stepperView;
    }

    public void setData(Sku.Price price) {
        StringBuilder sb = new StringBuilder();
        if (price.getAdult() > 0) {
            sb.append(price.getAdult() + "成人");
        }
        if (price.getChild() > 0) {
            sb.append(price.getChild() + "小孩");
        }
        sb.append("：￥").append(PriceUtils.formatPriceString(price.getPrice())).append("/").append(price.getUnit());
        this.priceTv.setText(sb.toString());
    }
}
