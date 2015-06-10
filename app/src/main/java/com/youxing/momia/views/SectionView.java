package com.youxing.momia.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.momia.R;

/**
 *
 * Created by Jun Deng on 15/6/9.
 */
public class SectionView extends LinearLayout {

    private TextView titleView;

    public SectionView(Context context) {
        this(context, null);
    }

    public SectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleView = (TextView)findViewById(R.id.title);
    }

    public void setTitle(String title) {
        this.titleView.setText(title);
    }
}
