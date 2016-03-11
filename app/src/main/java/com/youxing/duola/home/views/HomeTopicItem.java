package com.youxing.duola.home.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;
import com.youxing.duola.model.HomeModel;

/**
 * Created by Jun Deng on 16/3/10.
 */
public class HomeTopicItem extends LinearLayout {

    private TextView titleTv;
    private TextView subTitleTv;
    private TextView countTv;

    public HomeTopicItem(Context context) {
        super(context);
    }

    public HomeTopicItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static HomeTopicItem create(Context context) {
        return (HomeTopicItem) LayoutInflater.from(context).inflate(R.layout.layout_home_topic_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView) findViewById(R.id.title);
        subTitleTv = (TextView) findViewById(R.id.subTitle);
        countTv = (TextView) findViewById(R.id.count);
    }

    public void setData(HomeModel.HomeTopic topic) {
        titleTv.setText(topic.getTitle());
        subTitleTv.setText(topic.getSubTitle());
        countTv.setText(topic.getJoined() + "人在讨论");
    }
}
