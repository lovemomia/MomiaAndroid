package com.youxing.duola.home.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.youxing.common.app.Enviroment;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 16/3/10.
 */
public class HomeSubjectCoverItem extends LinearLayout {

    private YXNetworkImageView coverIv;

    public HomeSubjectCoverItem(Context context) {
        super(context);
    }

    public HomeSubjectCoverItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static HomeSubjectCoverItem create(Context context) {
        return (HomeSubjectCoverItem) LayoutInflater.from(context).inflate(R.layout.layout_home_subject_cover_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        coverIv = (YXNetworkImageView) findViewById(R.id.cover);

        int width = Enviroment.screenWidth(getContext());
        setLayoutParams(new AbsListView.LayoutParams(width, width * 180 / 320));
    }

    public void setData(String coverUrl) {
        coverIv.setImageUrl(coverUrl);
    }
}
