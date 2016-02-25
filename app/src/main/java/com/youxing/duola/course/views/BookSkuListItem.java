package com.youxing.duola.course.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;
import com.youxing.duola.model.CourseSkuListModel;

/**
 * Created by Jun Deng on 16/2/23.
 */
public class BookSkuListItem extends LinearLayout {

    private TextView titleTv;
    private TextView addressTv;
    private TextView timesTv;
    private TextView stockTv;
    private View gouIv;

    public BookSkuListItem(Context context) {
        super(context);
    }

    public BookSkuListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView) findViewById(R.id.title);
        addressTv = (TextView) findViewById(R.id.address);
        timesTv = (TextView) findViewById(R.id.times);
        stockTv = (TextView) findViewById(R.id.stock);
        gouIv = findViewById(R.id.gou);
    }

    public static BookSkuListItem create(Context context) {
        return (BookSkuListItem) LayoutInflater.from(context).inflate(R.layout.layout_book_sku_list_item, null);
    }

    public void setData(CourseSkuListModel.CourseSku sku) {
        titleTv.setText(sku.getPlace().getName());
        addressTv.setText(sku.getPlace().getAddress());
        timesTv.setText(sku.getTime());
        stockTv.setText("仅剩" + sku.getStock() + "个名额");
    }

    public void setSelect(boolean select) {
        gouIv.setVisibility(select ? View.VISIBLE : View.GONE);
    }
}
