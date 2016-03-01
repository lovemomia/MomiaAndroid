package com.youxing.duola.mine.views;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 16/2/29.
 */
public class AddReviewContentItem extends LinearLayout {

    private EditText contentEt;
    private GridLayout gridLay;
    private View addView;

    public AddReviewContentItem(Context context) {
        super(context);
    }

    public AddReviewContentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentEt = (EditText) findViewById(R.id.content);
        gridLay = (GridLayout) findViewById(R.id.grid);
        addView = findViewById(R.id.add);
    }

    public static AddReviewContentItem create(Context context) {
        return (AddReviewContentItem) LayoutInflater.from(context).inflate(R.layout.layout_add_review_content_item, null);
    }

    public EditText getContentEt() {
        return contentEt;
    }

    public GridLayout getGridLay() {
        return gridLay;
    }

    public View getAddView() {
        return addView;
    }
}
