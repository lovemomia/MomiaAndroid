package com.youxing.duola.course.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Course;

/**
 * Created by Jun Deng on 15/8/7.
 */
public class CourseDetailTeacherItem extends LinearLayout {

    private LinearLayout titleLay;
    private YXNetworkImageView iconIv;
    private TextView titleTv;
    private TextView nameTv;
    private TextView educationTv;
    private TextView contentTv;

    public CourseDetailTeacherItem(Context context) {
        this(context, null);
    }

    public CourseDetailTeacherItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static CourseDetailTeacherItem create(Context context) {
        return (CourseDetailTeacherItem)LayoutInflater.from(context).inflate(R.layout.layout_course_detail_teacher_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleLay = (LinearLayout) findViewById(R.id.title_lay);
        iconIv = (YXNetworkImageView) findViewById(R.id.icon);
        titleTv = (TextView) findViewById(R.id.title);
        nameTv = (TextView) findViewById(R.id.name);
        educationTv = (TextView) findViewById(R.id.education);
        contentTv = (TextView) findViewById(R.id.content);
    }

    public void setData(Course.CourseTeacher teacher) {
        setData(teacher, false);
    }

    public void setData(Course.CourseTeacher teacher, boolean showTitle) {
        titleLay.setVisibility(showTitle ? View.VISIBLE : View.GONE);
        iconIv.setImageUrl(teacher.getAvatar());
        nameTv.setText(teacher.getName());
        educationTv.setText(teacher.getEducation());
        contentTv.setText(teacher.getExperience());
    }
}
