package com.youxing.duola.home.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.youxing.common.views.CircularImage;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 16/2/3.
 */
public class HomeTitleBar extends android.support.v7.widget.Toolbar {

    private CircularImage avatarIv;
    private TextView nameTv;
    private TextView ageTv;
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
        avatarIv = (CircularImage) findViewById(R.id.avatar);
        nameTv = (TextView) findViewById(R.id.name);
        ageTv = (TextView) findViewById(R.id.age);
        cityTv = (TextView) findViewById(R.id.city);
    }

    public CircularImage getAvatarIv() {
        return avatarIv;
    }

    public TextView getNameTv() {
        return nameTv;
    }

    public TextView getAgeTv() {
        return ageTv;
    }

    public TextView getCityTv() {
        return cityTv;
    }
}
