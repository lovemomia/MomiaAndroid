package com.youxing.duola.home.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Course;

/**
 * Created by Jun Deng on 16/3/10.
 */
public class HomeSubjectContentCourseItem extends LinearLayout {

    private YXNetworkImageView iconIv;
    private TextView titleTv;
    private TextView ageTv;
    private TextView joinedTv;

    public HomeSubjectContentCourseItem(Context context) {
        super(context);
    }

    public HomeSubjectContentCourseItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static HomeSubjectContentCourseItem create(Context context) {
        return (HomeSubjectContentCourseItem) LayoutInflater.from(context).inflate(R.layout.layout_home_subject_content_couse_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (YXNetworkImageView) findViewById(R.id.icon);
        titleTv = (TextView) findViewById(R.id.title);
        ageTv = (TextView) findViewById(R.id.age);
        joinedTv = (TextView) findViewById(R.id.join);
    }

    public void setData(Course course) {
        iconIv.setImageUrl(course.getCover());
        titleTv.setText(course.getTitle());
        ageTv.setText(course.getAge());
        joinedTv.setText(course.getJoined() + "人已参加");
    }
}
