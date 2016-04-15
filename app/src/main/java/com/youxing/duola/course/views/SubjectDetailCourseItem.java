package com.youxing.duola.course.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Course;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class SubjectDetailCourseItem extends FrameLayout {

    @Bind(R.id.cover) YXNetworkImageView coverIv;
    @Bind(R.id.lesson) TextView lessonTv;
    @Bind(R.id.title) TextView titleTv;
    @Bind(R.id.subTitle) TextView subTitleTv;
    @Bind(R.id.number) TextView numberTv;

    public SubjectDetailCourseItem(Context context) {
        super(context);
    }

    public SubjectDetailCourseItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        int width = Enviroment.screenWidth(getContext());
        setLayoutParams(new AbsListView.LayoutParams(width, width * 180 / 300));
    }

    public static SubjectDetailCourseItem create(Context context, View convertView) {
        if (convertView instanceof SubjectDetailCourseItem) {
            return (SubjectDetailCourseItem)convertView;
        }
        return (SubjectDetailCourseItem) LayoutInflater.from(context).inflate(R.layout.layout_subject_detail_course_item, null);
    }

    public void setData(Course course) {
        coverIv.setImageUrl(course.getCover());
        titleTv.setText(course.getTitle());
        subTitleTv.setText(course.getAge());
        numberTv.setText(course.getJoined() + "人已参加");
    }

    public void setLessonIndex(int index) {
        lessonTv.setText("LESSON " + index);
    }

}
