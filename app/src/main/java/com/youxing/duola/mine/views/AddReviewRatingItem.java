package com.youxing.duola.mine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 16/2/26.
 */
public class AddReviewRatingItem extends LinearLayout {

    private TextView titleTv;
    private RatingBar ratingBar;

    public AddReviewRatingItem(Context context) {
        super(context);
    }

    public AddReviewRatingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView) findViewById(R.id.title);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

    public static AddReviewRatingItem create(Context context, int style) {
        if (style == 0) {
            return (AddReviewRatingItem) LayoutInflater.from(context).inflate(R.layout.layout_rating_list_item, null);
        }
        return (AddReviewRatingItem) LayoutInflater.from(context).inflate(R.layout.layout_rating_list_item2, null);
    }

    public static AddReviewRatingItem create(Context context) {
        return (AddReviewRatingItem) LayoutInflater.from(context).inflate(R.layout.layout_rating_list_item, null);
    }

    public TextView getTitleTv() {
        return titleTv;
    }

    public RatingBar getRatingBar() {
        return ratingBar;
    }
}
