package com.youxing.duola.home.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.duola.R;
import com.youxing.duola.model.Course;
import com.youxing.duola.model.HomeModel;

/**
 * Created by Jun Deng on 16/3/10.
 */
public class HomeSubjectContentItem extends LinearLayout {

    private TextView titleTv;
    private ViewGroup contentLay;

    public HomeSubjectContentItem(Context context) {
        super(context);
    }

    public HomeSubjectContentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static HomeSubjectContentItem create(Context context) {
        return (HomeSubjectContentItem) LayoutInflater.from(context).inflate(R.layout.layout_home_subject_content_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView) findViewById(R.id.title);
        contentLay = (ViewGroup) findViewById(R.id.content);
    }

    public void setData(HomeModel.HomeSubject subject) {
        titleTv.setText(subject.getCoursesTitle());

        for (int i = 0; i < subject.getCourses().size(); i++) {
            if (i > 2) {
                break;
            }
            final Course course = subject.getCourses().get(i);
            HomeSubjectContentCourseItem item = HomeSubjectContentCourseItem.create(getContext(), subject.getSubjectCourseType());
            item.setLayoutParams(new LayoutParams(Enviroment.screenWidth(getContext())/subject.getCourses().size(),
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            item.setData(course, subject.getSubjectCourseType());
            contentLay.addView(item);

            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://coursedetail?id=" + course.getId())));
                }
            });
        }

    }

}
