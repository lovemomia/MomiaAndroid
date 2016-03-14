package com.youxing.duola.course.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/8/7.
 */
public class CourseDetailNoticeItem extends LinearLayout {

    private TextView titleTv;
    private TextView contentTv;

    public CourseDetailNoticeItem(Context context) {
        this(context, null);
    }

    public CourseDetailNoticeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static CourseDetailNoticeItem create(Context context) {
        return (CourseDetailNoticeItem)LayoutInflater.from(context).inflate(R.layout.layout_course_detail_notice_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView) findViewById(R.id.title);
        contentTv = (TextView) findViewById(R.id.content);
    }

    public void setData(String notice) {
        contentTv.setText(notice);
    }
}
