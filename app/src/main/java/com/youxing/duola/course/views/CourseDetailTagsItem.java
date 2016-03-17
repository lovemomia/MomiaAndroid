package com.youxing.duola.course.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/8/7.
 */
public class CourseDetailTagsItem extends LinearLayout {

    public CourseDetailTagsItem(Context context) {
        this(context, null);
    }

    public CourseDetailTagsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static CourseDetailTagsItem create(Context context) {
        return new CourseDetailTagsItem(context);
    }

    public void setData(String[] tags) {
        int padding = UnitTools.dip2px(getContext(), 12);
        this.setPadding(padding, padding, padding, padding);
        for (String tag : tags) {
            TextView tv = new TextView(getContext());
            tv.setText(tag);
            tv.setTextColor(getResources().getColor(R.color.text_light_gray));
            tv.setTextSize(12);
            Drawable img = getResources().getDrawable(R.drawable.ic_product_header_tag);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tv.setCompoundDrawables(img, null, null, null);
            tv.setCompoundDrawablePadding(UnitTools.dip2px(getContext(), 6));
            tv.setPadding(0, 0, 40, 0);
            this.addView(tv);
        }
    }
}
