package com.youxing.duola.course.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.github.mmin18.widget.FlexLayout;
import com.youxing.duola.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class SubjectDetailCourseHeaderItem extends FlexLayout {

    @Bind(R.id.title) TextView titleTv;

    public SubjectDetailCourseHeaderItem(Context context) {
        super(context);
    }

    public SubjectDetailCourseHeaderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public static SubjectDetailCourseHeaderItem create(Context context) {
        return (SubjectDetailCourseHeaderItem) LayoutInflater.from(context).inflate(R.layout.layout_subject_detail_course_header_item, null);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }
}
