package com.youxing.duola.course.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Course;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class SubjectDetailCourseItem extends FrameLayout {

    protected YXNetworkImageView coverIv;
    protected TextView titleTv;
    protected TextView subTitleTv;
    protected TextView numberTv;

    public SubjectDetailCourseItem(Context context) {
        super(context);
    }

    public SubjectDetailCourseItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        coverIv = (YXNetworkImageView) findViewById(R.id.cover);
        coverIv.setDefaultImageResId(R.drawable.bg_default_image);
        titleTv = (TextView) findViewById(R.id.title);
        subTitleTv = (TextView) findViewById(R.id.subTitle);
        numberTv = (TextView) findViewById(R.id.number);

        int width = Enviroment.screenWidth(getContext());
        setLayoutParams(new AbsListView.LayoutParams(width, width * 180 / 300));
    }

    public static SubjectDetailCourseItem create(Context context) {
        return (SubjectDetailCourseItem) LayoutInflater.from(context).inflate(R.layout.layout_subject_detail_course_item, null);
    }

    public void setData(Course course) {
        coverIv.setImageUrl(course.getCover());
        titleTv.setText(course.getTitle());
        subTitleTv.setText(course.getAge());
        numberTv.setText(course.getJoined() + "人已参加");
    }

}
