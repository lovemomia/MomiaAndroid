package com.youxing.duola.home.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Course;
import com.youxing.duola.utils.PriceUtils;

/**
 * Created by Jun Deng on 15/6/15.
 */
public class HomeListItem extends LinearLayout {

    private View coverLay;
    private YXNetworkImageView coverIv;
    private TextView titleTv;
    private TextView addressTv;
    private TextView dateTv;
    private TextView numberTv;
    private TextView priceTv;
    private ImageView soldoutIv;

    public HomeListItem(Context context) {
        this(context, null);
    }

    public HomeListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static HomeListItem create(Context context) {
        return (HomeListItem)LayoutInflater.from(context).inflate(R.layout.layout_home_list_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        coverLay = findViewById(R.id.cover_lay);
        coverIv = (YXNetworkImageView)findViewById(R.id.cover);
        titleTv = (TextView) findViewById(R.id.title);
        addressTv = (TextView) findViewById(R.id.address);
        dateTv = (TextView) findViewById(R.id.date);
        numberTv = (TextView) findViewById(R.id.number);
        priceTv = (TextView) findViewById(R.id.price);
        soldoutIv = (ImageView) findViewById(R.id.sold_out);
    }

    public void setData(Course course) {
        int width = Enviroment.screenWidth(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width * 1/2);
        coverLay.setLayoutParams(lp);
        coverIv.setDefaultImageResId(R.drawable.bg_default_image);
        coverIv.setImageUrl(course.getCover());
        titleTv.setText(course.getTitle());
        addressTv.setText(course.getAge() + " | " + course.getRegion());
        dateTv.setText(course.getSubject());
        if (course.getJoined() > 0) {
            numberTv.setText(course.getJoined() + "人报名");
            numberTv.setVisibility(View.VISIBLE);
        } else {
            numberTv.setVisibility(View.GONE);
        }
        priceTv.setText(PriceUtils.formatPriceString(course.getPrice()));
        if (course.getStatus() == 2) {
            soldoutIv.setVisibility(View.VISIBLE);
        } else {
            soldoutIv.setVisibility(View.GONE);
        }
    }
}
