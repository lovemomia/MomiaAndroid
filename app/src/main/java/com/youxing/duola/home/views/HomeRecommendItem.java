package com.youxing.duola.home.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.HomeModel;

/**
 * Created by Jun Deng on 15/6/15.
 */
public class HomeRecommendItem extends LinearLayout {

    private YXNetworkImageView iconIv;
    private TextView titleTv;
    private TextView subTitleTv;

    public HomeRecommendItem(Context context) {
        this(context, null);
    }

    public HomeRecommendItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static HomeRecommendItem create(Context context) {
        return (HomeRecommendItem)LayoutInflater.from(context).inflate(R.layout.layout_home_recommend_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (YXNetworkImageView)findViewById(R.id.icon);
        titleTv = (TextView) findViewById(R.id.title);
        subTitleTv = (TextView) findViewById(R.id.subTitle);
    }

    public void setData(HomeModel.HomeRecommend recommend) {
        iconIv.setImageUrl(recommend.getCover());
        titleTv.setText(recommend.getTitle());
        subTitleTv.setText(recommend.getDesc());
    }
}
