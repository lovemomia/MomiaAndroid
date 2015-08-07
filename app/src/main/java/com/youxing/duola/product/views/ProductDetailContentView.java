package com.youxing.duola.product.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Product;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/7.
 */
public class ProductDetailContentView extends LinearLayout {

    public ProductDetailContentView(Context context) {
        this(context, null);
    }

    public ProductDetailContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ProductDetailContentView create(Context context) {
        ProductDetailContentView view = new ProductDetailContentView(context);
        view.setOrientation(LinearLayout.VERTICAL);
        int padding = UnitTools.dip2px(context, 10);
        view.setPadding(padding, padding, padding, 0);
        return view;
    }

    public void setData(Product.ContentItem contentItem) {
        List<Product.BodyItem> bodys = contentItem.getBody();
        for (int i = 0; i < bodys.size(); i++) {
            Product.BodyItem body = bodys.get(i);

            if (!TextUtils.isEmpty(body.getLink())) {
                // 链接
                TextView tv = new TextView(getContext());
                tv.setText(body.getText());
                tv.setTextSize(14);
                tv.setTextColor(getResources().getColor(R.color.text_deep_gray));
                this.addView(tv);

            } else if (!TextUtils.isEmpty(body.getImg())) {
                // 图片
                YXNetworkImageView iv = new YXNetworkImageView(getContext());
                int width = Enviroment.screenWidth(getContext()) - UnitTools.dip2px(getContext(), 20);
                LayoutParams lp = new LayoutParams(width, width * 3/4);
                lp.setMargins(0, 0, 0, UnitTools.dip2px(getContext(), 10));
                iv.setLayoutParams(lp);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setBackgroundResource(R.color.bg_image);
                iv.setImageUrl(body.getImg());

                addView(iv);
            } else {
                if (!TextUtils.isEmpty(body.getLabel())) {
                    // 小标题
                    TextView tv = new TextView(getContext());
                    tv.setText(body.getLabel());
                    tv.setLineSpacing(UnitTools.dip2px(getContext(), 8), 1);
                    tv.setTextSize(13);
                    tv.setTextColor(getResources().getColor(R.color.text_gray));

                    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.setMargins(lp.leftMargin, 0, lp.rightMargin, UnitTools.dip2px(getContext(), 8));
                    tv.setLayoutParams(lp);

                    this.addView(tv);
                }
                if (!TextUtils.isEmpty(body.getText())) {
                    // 文本内容
                    TextView tv = new TextView(getContext());
                    tv.setLineSpacing(UnitTools.dip2px(getContext(), 8), 1);
                    tv.setTextSize(13);
                    tv.setTextColor(getResources().getColor(R.color.text_deep_gray));
                    if ("ol".equals(contentItem.getStyle())) {
                        tv.setText("- " + body.getText());
                        tv.setPadding(0, 0, 0, UnitTools.dip2px(getContext(), 10));
                        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        lp.setMargins(lp.leftMargin, 0, lp.rightMargin, UnitTools.dip2px(getContext(), 8));
                        tv.setLayoutParams(lp);

                    } else {
                        tv.setText(body.getText());
                        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        lp.setMargins(lp.leftMargin, 0, lp.rightMargin, UnitTools.dip2px(getContext(), 8));
                        tv.setLayoutParams(lp);
                    }
                    this.addView(tv);
                }
            }
        }
    }
}
