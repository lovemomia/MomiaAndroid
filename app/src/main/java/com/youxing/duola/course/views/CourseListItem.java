package com.youxing.duola.course.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.BookingSubjectListModel;
import com.youxing.duola.model.Course;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class CourseListItem extends LinearLayout {

    protected YXNetworkImageView iconIv;
    protected TextView titleTv;
    protected TextView dateTv;
    protected TextView timesTv;

    public CourseListItem(Context context) {
        super(context);
    }

    public CourseListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (YXNetworkImageView) findViewById(R.id.icon);
        iconIv.setDefaultImageResId(R.drawable.bg_default_image);
        titleTv = (TextView) findViewById(R.id.title);
        dateTv = (TextView) findViewById(R.id.date);
        timesTv = (TextView) findViewById(R.id.times);
    }

    public static CourseListItem create(Context context) {
        return (CourseListItem) LayoutInflater.from(context).inflate(R.layout.layout_course_list_item, null);
    }

    public void setData(Course course) {
        iconIv.setImageUrl(course.getCover());
        titleTv.setText(course.getTitle());
        dateTv.setText(course.getAge() + " | " + course.getScheduler());
        if (course.getPlace() != null && !TextUtils.isEmpty(course.getPlace().getName())) {
            timesTv.setText(course.getPlace().getName());
        } else {
            timesTv.setText(course.getRegion());
        }
    }

}
