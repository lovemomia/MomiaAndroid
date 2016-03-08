package com.youxing.duola.course.views;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.CircularImage;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.CourseSkuListModel;
import com.youxing.duola.model.ReviewList;

/**
 * Created by Jun Deng on 16/2/23.
 */
public class CourseReviewListItem extends LinearLayout {

    private CircularImage avatar;
    private TextView nameTv;
    private TextView dateTv;
    private TextView childrenTv;
    private RatingBar ratingBar;
    private TextView contentTv;
    private GridLayout gridLayout;

    public CourseReviewListItem(Context context) {
        super(context);
    }

    public CourseReviewListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        avatar = (CircularImage) findViewById(R.id.avatar);
        nameTv = (TextView) findViewById(R.id.name);
        dateTv = (TextView) findViewById(R.id.date);
        childrenTv = (TextView) findViewById(R.id.children);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        contentTv = (TextView) findViewById(R.id.content);
        gridLayout = (GridLayout) findViewById(R.id.grid);
    }

    public static CourseReviewListItem create(Context context) {
        return (CourseReviewListItem) LayoutInflater.from(context).inflate(R.layout.layout_course_review_list_item, null);
    }

    public void setData(ReviewList.Review review) {
        avatar.setImageUrl(review.getAvatar());
        nameTv.setText(review.getNickName());
        dateTv.setText(review.getAddTime());
        ratingBar.setRating(review.getStar());
        contentTv.setText(review.getContent());

        gridLayout.removeAllViews();
        int margin = UnitTools.dip2px(getContext(), 10);
        int width = (Enviroment.screenWidth(getContext()) - 4 * margin - UnitTools.dip2px(getContext(), 65)) / 3;
        for (int i = 0; i < review.getImgs().size(); i++) {
            String url = review.getImgs().get(i);
            YXNetworkImageView iv = new YXNetworkImageView(getContext());
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();

            lp.height = width;
            lp.width = lp.height;

            lp.setMargins(margin/2, margin/2, margin/2, margin/2);
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageUrl(url);
            iv.setDefaultImageResId(R.drawable.bg_default_image);

            gridLayout.addView(iv, 0);

        }
    }

}
