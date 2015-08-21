package com.youxing.duola.order.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;
import com.youxing.duola.model.Sku;
import com.youxing.duola.utils.PriceUtils;

/**
 * Created by Jun Deng on 15/8/11.
 */
public class OrderSkuItem extends LinearLayout {

    private TextView dateTv;
    private TextView priceTv;
    private ImageView selectIv;

    private boolean select;
    private boolean isSelectAble;

    public OrderSkuItem(Context context) {
        this(context, null);
    }

    public OrderSkuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static OrderSkuItem create(Context context) {
        return (OrderSkuItem) LayoutInflater.from(context).inflate(R.layout.layout_order_sku, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dateTv = (TextView) findViewById(R.id.order_sku_date);
        priceTv = (TextView) findViewById(R.id.order_sku_price);
        selectIv = (ImageView) findViewById(R.id.order_sku_select);
    }

    public void setData(Sku sku) {
        isSelectAble = true;
        dateTv.setText(sku.getTime());
        StringBuilder sb = new StringBuilder("￥" + PriceUtils.formatPriceString(sku.getMinPrice()) + "起");
        sb.append("    ");
        if (sku.getType() == 1) { //无上限
            if (sku.getLimit() != 0) {
                sb.append("每人限" + sku.getLimit() + "单");
            }

        } else {
            if (sku.getStock() == 0) {
                sb.append("名额已满");
                isSelectAble = false;
            } else {
                sb.append("仅剩" + sku.getStock() + "名额");
            }

            if (sku.getLimit() != 0) {
                sb.append("，每人限" + sku.getLimit() + "单");
            }
        }
        priceTv.setText(sb.toString());
    }

    public void setSelect(boolean select) {
        this.select = select;
        if (select) {
            selectIv.setVisibility(View.VISIBLE);
        } else {
            selectIv.setVisibility(View.GONE);
        }
    }

    public boolean isSelectAble() {
        return isSelectAble;
    }

    public boolean isSelect() {
        return select;
    }
}
