package com.youxing.duola.mine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.CircleImageView;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/8/24.
 */
public class MineHeaderView extends LinearLayout {

    private CircleImageView avartaIv;
    private TextView nameTv;
    private TextView ageTv;

    public MineHeaderView(Context context) {
        super(context);
    }

    public MineHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static MineHeaderView create(Context context) {
        return (MineHeaderView) LayoutInflater.from(context).inflate(R.layout.layout_mine_header, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        avartaIv = (CircleImageView) findViewById(R.id.avatar);
        avartaIv.setDefaultImageResId(R.drawable.ic_default_avatar);
        nameTv = (TextView) findViewById(R.id.name);
        ageTv = (TextView) findViewById(R.id.age);
    }

    public CircleImageView getAvartaIv() {
        return avartaIv;
    }

    public TextView getNameTv() {
        return nameTv;
    }

    public TextView getAgeTv() {
        return ageTv;
    }
}
