package com.youxing.duola.mine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 16/2/26.
 */
public class RatingListItem extends LinearLayout {

    public RatingListItem(Context context) {
        super(context);
    }

    public RatingListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public static RatingListItem create(Context context) {
        return (RatingListItem) LayoutInflater.from(context).inflate(R.layout.layout_order_list_item, null);
    }

}
