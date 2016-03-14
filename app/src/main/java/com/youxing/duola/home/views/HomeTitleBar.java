package com.youxing.duola.home.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.CircleImageView;
import com.youxing.common.views.CircularImage;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 16/2/3.
 */
public class HomeTitleBar extends android.support.v7.widget.Toolbar {

    private LinearLayout childLay;
    private CircleImageView avatarIv;
    private TextView nameTv;
    private TextView cityTv;

    public HomeTitleBar(Context context) {
        super(context);
    }

    public HomeTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childLay = (LinearLayout) findViewById(R.id.child_lay);
        avatarIv = (CircleImageView) findViewById(R.id.avatar);
        nameTv = (TextView) findViewById(R.id.name);
        cityTv = (TextView) findViewById(R.id.city);
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
