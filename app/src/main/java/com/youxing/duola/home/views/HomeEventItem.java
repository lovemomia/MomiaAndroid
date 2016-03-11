package com.youxing.duola.home.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.HomeModel;
import com.youxing.duola.model.Product;
import com.youxing.duola.utils.PriceUtils;

/**
 * Created by Jun Deng on 15/6/15.
 */
public class HomeEventItem extends LinearLayout implements View.OnClickListener {

    private View eventLeftLay;
    private View eventRightLay;
    private TextView titleTv;
    private TextView eventTitleLeftTv;
    private TextView eventDescLeftTv;
    private TextView eventTitleRightTv;
    private TextView eventDescRightTv;
    private YXNetworkImageView iconLeft;
    private YXNetworkImageView iconRight;

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
        eventLeftLay = findViewById(R.id.eventLeft);
        eventRightLay = findViewById(R.id.eventRight);
        titleTv = (TextView) findViewById(R.id.title);
        eventTitleLeftTv = (TextView) findViewById(R.id.eventTitleLeft);
        eventTitleRightTv = (TextView) findViewById(R.id.eventTitleRight);
        eventDescLeftTv = (TextView) findViewById(R.id.eventDescLeft);
        eventDescRightTv = (TextView) findViewById(R.id.eventDescRight);
        iconLeft = (YXNetworkImageView) findViewById(R.id.iconLeft);
        iconRight = (YXNetworkImageView) findViewById(R.id.iconRight);

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
