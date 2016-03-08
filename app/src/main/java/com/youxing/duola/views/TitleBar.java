package com.youxing.duola.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/8/6.
 */
public class TitleBar extends android.support.v7.widget.Toolbar {

    private TextView titleTv;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView) findViewById(R.id.toolbar_title);
        super.setTitle("");
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public TextView getTitleTv() {
        return titleTv;
    }

}
