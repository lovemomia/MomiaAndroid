package com.youxing.duola.product.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.CircularImage;
import com.youxing.duola.R;
import com.youxing.duola.model.Product;

import java.util.List;

/**
 * Created by Jun Deng on 15/6/16.
 */
public class ProductDetailPartersView extends LinearLayout {

    public ProductDetailPartersView(Context context) {
        this(context, null);
    }

    public ProductDetailPartersView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ProductDetailPartersView create(Context context) {
        return new ProductDetailPartersView(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setData(Product.Customers customers) {
        int padding = UnitTools.dip2px(getContext(), 10);
        this.setPadding(padding, UnitTools.dip2px(getContext(), 15), padding, UnitTools.dip2px(getContext(), 15));
        List<String> avatars = customers.getAvatars();
        for (String avatar : avatars) {
            CircularImage image = new CircularImage(getContext());
            LayoutParams lp = new LayoutParams(UnitTools.dip2px(getContext(), 40), UnitTools.dip2px(getContext(), 40));
            lp.setMargins(0, 0, UnitTools.dip2px(getContext(), 15), 0);
            image.setLayoutParams(lp);
            image.setImageUrl(avatar);
            image.setDefaultImageResId(R.drawable.ic_avatar_default);
            this.addView(image);
        }
    }
}
