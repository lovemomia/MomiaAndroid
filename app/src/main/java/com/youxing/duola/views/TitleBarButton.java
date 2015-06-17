package com.youxing.duola.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/6/15.
 */
public class TitleBarButton extends FrameLayout {

    private ImageView iconIv;

    public TitleBarButton(Context context) {
        super(context);
    }

    public TitleBarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (ImageView)findViewById(R.id.icon);
    }

    public void setIcon(int resId) {
        iconIv.setImageResource(resId);
    }
}
