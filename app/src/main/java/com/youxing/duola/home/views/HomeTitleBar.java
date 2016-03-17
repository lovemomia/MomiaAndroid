package com.youxing.duola.home.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.CircleImageView;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 16/2/3.
 */
public class HomeTitleBar extends LinearLayout {

    private Toolbar toolbar;
    private LinearLayout childLay;
    private CircleImageView avatarIv;
    private TextView nameTv;
    private TextView cityTv;
    private View shadowView;

    public HomeTitleBar(Context context) {
        super(context);
    }

    public HomeTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        childLay = (LinearLayout) findViewById(R.id.child_lay);
        avatarIv = (CircleImageView) findViewById(R.id.avatar);
        nameTv = (TextView) findViewById(R.id.name);
        cityTv = (TextView) findViewById(R.id.city);
        shadowView = findViewById(R.id.shadow);
        if (Build.VERSION.SDK_INT < 21) {
            shadowView.setVisibility(View.VISIBLE);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public LinearLayout getChildLay() {
        return childLay;
    }

    public CircleImageView getAvatarIv() {
        return avatarIv;
    }

    public TextView getNameTv() {
        return nameTv;
    }

    public TextView getCityTv() {
        return cityTv;
    }
}
