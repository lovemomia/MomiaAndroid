package com.youxing.duola.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.OrderDetailModel;
import com.youxing.duola.model.Product;
import com.youxing.duola.utils.PriceUtils;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class ProductListItem extends LinearLayout {

    private YXNetworkImageView iconIv;
    private TextView titleTv;
    private TextView dateTv;
    private TextView addressTv;
    private TextView priceTv;

    public ProductListItem(Context context) {
        this(context, null);
    }

    public ProductListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ProductListItem create(Context context) {
        return (ProductListItem) LayoutInflater.from(context).inflate(R.layout.layout_product_list_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (YXNetworkImageView) findViewById(R.id.icon);
        iconIv.setDefaultImageResId(R.drawable.bg_default_image);
        titleTv = (TextView) findViewById(R.id.title);
        dateTv = (TextView) findViewById(R.id.date);
        addressTv = (TextView) findViewById(R.id.address);
        priceTv = (TextView) findViewById(R.id.price);
    }

    public void setData(Product data) {
        iconIv.setImageUrl(data.getCover());
        titleTv.setText(data.getTitle());
        dateTv.setText(data.getScheduler());
        addressTv.setText(data.getAddress());
        priceTv.setText(PriceUtils.formatPriceString(data.getPrice()));
    }
}
