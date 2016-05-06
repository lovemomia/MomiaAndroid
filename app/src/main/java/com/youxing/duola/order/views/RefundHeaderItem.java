package com.youxing.duola.order.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;
import com.youxing.duola.model.Order;
import com.youxing.duola.utils.PriceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class RefundHeaderItem extends LinearLayout {

    @Bind(R.id.price) TextView priceTv;
    @Bind(R.id.number) TextView numberTv;
    @Bind(R.id.payType) TextView payTv;

    public RefundHeaderItem(Context context) {
        this(context, null);
    }

    public RefundHeaderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static RefundHeaderItem create(Context context) {
        return (RefundHeaderItem) LayoutInflater.from(context).inflate(R.layout.layout_refund_header, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setData(Order order) {
        priceTv.setText("¥" + PriceUtils.formatPriceString(order.getPayedFee()));
        numberTv.setText(String.valueOf(order.getCount()));
        payTv.setText(order.getPayType() == 1 ? "支付宝账户" : "微信账户");
    }


}
