package com.youxing.duola.home.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Product;
import com.youxing.duola.utils.PriceUtils;

/**
 * Created by Jun Deng on 15/6/15.
 */
public class HomeListItem extends RelativeLayout {

    private YXNetworkImageView coverIv;
    private TextView titleTv;
    private TextView addressTv;
    private TextView dateTv;
    private TextView numberTv;
    private TextView priceTv;

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
        coverIv = (YXNetworkImageView)findViewById(R.id.cover);
        titleTv = (TextView) findViewById(R.id.title);
        addressTv = (TextView) findViewById(R.id.address);
        dateTv = (TextView) findViewById(R.id.date);
        numberTv = (TextView) findViewById(R.id.number);
        priceTv = (TextView) findViewById(R.id.price);
    }

    public void setData(Product product) {
        coverIv.setImageUrl(product.getCover());
        titleTv.setText(product.getTitle());
        addressTv.setText(product.getRegion());
        dateTv.setText(product.getScheduler());
        numberTv.setText(product.getJoined() + "人报名");
        priceTv.setText(PriceUtils.formatPriceString(product.getPrice()));
    }
}
