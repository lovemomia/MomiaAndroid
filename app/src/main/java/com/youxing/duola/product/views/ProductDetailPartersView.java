package com.youxing.duola.product.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.CircularImage;
import com.youxing.duola.R;
import com.youxing.duola.model.Product;

import java.util.List;

/**
 * Created by Jun Deng on 15/6/16.
 */
public class ProductDetailPartersView extends LinearLayout {

    private TextView numberTv;
    private ViewGroup partersLay;

    public ProductDetailPartersView(Context context) {
        this(context, null);
    }

    public ProductDetailPartersView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ProductDetailPartersView create(Context context) {
        return (ProductDetailPartersView) LayoutInflater.from(context).inflate(R.layout.layout_product_detail_parters, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        numberTv = (TextView) findViewById(R.id.number);
        partersLay = (ViewGroup) findViewById(R.id.parters);
    }

    public void setData(Product.Customers customers) {
        numberTv.setText(customers.getText());
        List<String> avatars = customers.getAvatars();
        for (String avatar : avatars) {
            CircularImage image = new CircularImage(getContext());
            LayoutParams lp = new LayoutParams(UnitTools.px2dip(getContext(), 40), UnitTools.px2dip(getContext(), 40));
            lp.setMargins(0, 0, UnitTools.px2dip(getContext(), 15), 0);
            image.setLayoutParams(lp);
            image.setImageUrl(avatar);
            partersLay.addView(image);
        }
    }
}
