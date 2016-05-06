package com.youxing.duola.order.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;

public class ApplyRefundCouseItem extends LinearLayout {

    private TextView titleTv;
    private CheckBox checkBox;

    public ApplyRefundCouseItem(Context context) {
        this(context, null);
    }

    public ApplyRefundCouseItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ApplyRefundCouseItem create(Context context) {
        ApplyRefundCouseItem view = (ApplyRefundCouseItem) LayoutInflater.from(context).inflate(R.layout.layout_apply_refund_couse_item, null);
        return view;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView)findViewById(R.id.title);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

}
