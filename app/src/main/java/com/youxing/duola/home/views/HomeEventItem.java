package com.youxing.duola.home.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.HomeModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jun Deng on 15/6/15.
 */
public class HomeEventItem extends LinearLayout implements View.OnClickListener {

    @Bind(R.id.eventLeft) View eventLeftLay;
    @Bind(R.id.eventRight) View eventRightLay;
    @Bind(R.id.title) TextView titleTv;
    @Bind(R.id.eventTitleLeft) TextView eventTitleLeftTv;
    @Bind(R.id.eventDescLeft) TextView eventDescLeftTv;
    @Bind(R.id.eventTitleRight) TextView eventTitleRightTv;
    @Bind(R.id.eventDescRight) TextView eventDescRightTv;
    @Bind(R.id.iconLeft) YXNetworkImageView iconLeft;
    @Bind(R.id.iconRight) YXNetworkImageView iconRight;

    private HomeModel model;

    public HomeEventItem(Context context) {
        this(context, null);
    }

    public HomeEventItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static HomeEventItem create(Context context) {
        return (HomeEventItem)LayoutInflater.from(context).inflate(R.layout.layout_home_event_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        eventLeftLay.setOnClickListener(this);
        eventRightLay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (model == null) {
            return;
        }
        if (v.getId() == R.id.eventLeft) {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(model.getData().getEvents().get(0).getAction())));

        } else {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(model.getData().getEvents().get(1).getAction())));

        }
    }

    public void setData(HomeModel model) {
        this.model = model;

        titleTv.setText(model.getData().getEventsTitle());
        if (model.getData().getEvents().size() < 2) {
            return;
        }

        HomeModel.HomeEvent left = model.getData().getEvents().get(0);
        eventTitleLeftTv.setText(left.getTitle());
        eventDescLeftTv.setText(left.getDesc());
        iconLeft.setImageUrl(left.getImg());


        HomeModel.HomeEvent right = model.getData().getEvents().get(1);
        eventTitleRightTv.setText(right.getTitle());
        eventDescRightTv.setText(right.getDesc());
        iconRight.setImageUrl(right.getImg());
    }
}
