package com.youxing.duola.course.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;
import com.youxing.duola.model.Course;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class CourseDetailPlaceItem extends LinearLayout {

    protected TextView titleTv;
    protected TextView dateTv;
    protected TextView addressTv;

    public CourseDetailPlaceItem(Context context) {
        super(context);
    }

    public CourseDetailPlaceItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView) findViewById(R.id.title);
        dateTv = (TextView) findViewById(R.id.date);
        addressTv = (TextView) findViewById(R.id.address);
    }

    public static CourseDetailPlaceItem create(Context context) {
        return (CourseDetailPlaceItem) LayoutInflater.from(context).inflate(R.layout.layout_course_detail_place_item, null);
    }

    public void setData(Course.CoursePlace place) {
        titleTv.setText(place.getName());
        dateTv.setText(place.getScheduler());
        addressTv.setText(place.getAddress());
    }

}
