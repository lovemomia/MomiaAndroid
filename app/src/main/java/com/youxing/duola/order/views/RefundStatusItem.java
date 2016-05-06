package com.youxing.duola.order.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;
import com.youxing.duola.model.Order;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class RefundStatusItem extends LinearLayout {

    @Bind(R.id.step_1_text) TextView step1Tv;
    @Bind(R.id.step_1_line) View step1Line;
    @Bind(R.id.step_1_title) TextView step1TitleTv;
    @Bind(R.id.step_1_subtitle) TextView step1SubTitleTv;
    @Bind(R.id.step_2_text) TextView step2Tv;
    @Bind(R.id.step_2_line) View step2Line;
    @Bind(R.id.step_2_title) TextView step2TitleTv;
    @Bind(R.id.step_2_subtitle) TextView step2SubTitleTv;
    @Bind(R.id.step_3_text) TextView step3Tv;
    @Bind(R.id.step_3_title) TextView step3TitleTv;
    @Bind(R.id.step_3_subtitle) TextView step3SubTitleTv;

    public RefundStatusItem(Context context) {
        this(context, null);
    }

    public RefundStatusItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static RefundStatusItem create(Context context) {
        return (RefundStatusItem) LayoutInflater.from(context).inflate(R.layout.layout_refund_status, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setData(Order order) {
        //5待退款 6已退款 7 申请通过

        if (order.getStatus() == 5 || order.getStatus() == 6 || order.getStatus() == 7) {
            step1Tv.setBackgroundResource(R.drawable.bg_shape_green_circle);
            step1TitleTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            step1SubTitleTv.setTextColor(getResources().getColor(R.color.text_deep_gray));

            if (order.getStatus() == 5) {
                return;
            }

            step2Tv.setBackgroundResource(R.drawable.bg_shape_green_circle);
            step2TitleTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            step2SubTitleTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            step1Line.setBackgroundResource(R.color.text_blue);

            if (order.getStatus() == 7) {
                return;
            }

            step3Tv.setBackgroundResource(R.drawable.bg_shape_green_circle);
            step3TitleTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            step3SubTitleTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            step2Line.setBackgroundResource(R.color.text_blue);
        }
    }


}
