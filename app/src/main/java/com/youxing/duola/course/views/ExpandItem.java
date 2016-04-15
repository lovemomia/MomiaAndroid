package com.youxing.duola.course.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jun Deng on 16/4/14.
 */
public class ExpandItem extends LinearLayout {

    @Bind(R.id.expand_btn) TextView expandBtn;

    public ExpandItem(Context context) {
        super(context);
    }

    public ExpandItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public static ExpandItem create(Context context) {
        return (ExpandItem) LayoutInflater.from(context).inflate(R.layout.layout_expand_item, null);
    }

    public void setExpand(boolean expand) {
        if (expand) {
            expandBtn.setText("点击收起");
            Drawable drawable = getResources().getDrawable(R.drawable.ic_black_arrow_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            expandBtn.setCompoundDrawables(drawable, null, null, null);
        } else {
            expandBtn.setText("点击展开");
            Drawable drawable = getResources().getDrawable(R.drawable.ic_black_arrow_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            expandBtn.setCompoundDrawables(drawable, null, null, null);
        }
    }

}
