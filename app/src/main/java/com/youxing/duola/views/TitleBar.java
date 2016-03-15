package com.youxing.duola.views;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/8/6.
 */
public class TitleBar extends LinearLayout {

    private Toolbar toolbar;
    private TextView titleTv;
    private View shadowView;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTv = (TextView) findViewById(R.id.toolbar_title);
        shadowView = findViewById(R.id.shadow);
        toolbar.setTitle("");
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public TextView getTitleTv() {
        return titleTv;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
