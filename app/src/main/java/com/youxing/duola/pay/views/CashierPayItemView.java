package com.youxing.duola.pay.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/6/10.
 */
public class CashierPayItemView extends LinearLayout {

    private ImageView iconIv;
    private TextView titleTv;
    private TextView subTitleTv;
    private CheckBox checkBoxCb;

    public CashierPayItemView(Context context) {
        this(context, null);
    }

    public CashierPayItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (ImageView)findViewById(R.id.icon);
        titleTv = (TextView)findViewById(R.id.title);
        subTitleTv = (TextView)findViewById(R.id.subTitle);
        checkBoxCb = (CheckBox)findViewById(R.id.checkbox);
    }

    public static CashierPayItemView create(Context context) {
        return (CashierPayItemView) LayoutInflater.from(context).inflate(R.layout.layout_cashier_item_pay, null);
    }

    public void setIcon(int resId) {
        iconIv.setImageResource(resId);
    }

    public void setTitle(String text) {
        titleTv.setText(text);
    }

    public void setSubTitle(String text) {
        subTitleTv.setText(text);
    }

    public void setChecked(boolean checked) {
        checkBoxCb.setChecked(checked);
    }

    public boolean isChecked() {
        return checkBoxCb.isChecked();
    }

}
