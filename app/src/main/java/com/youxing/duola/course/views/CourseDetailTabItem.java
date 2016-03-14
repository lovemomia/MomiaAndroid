package com.youxing.duola.course.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 16/3/7.
 */
public class CourseDetailTabItem extends FrameLayout implements View.OnClickListener {

    private TextView tabLeftTv;
    private TextView tabCenterTv;
    private TextView tabRightTv;
    private View tabBottomView;

    private OnTabChangeListener listener;

    public CourseDetailTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseDetailTabItem(Context context) {
        super(context);
    }

    public static CourseDetailTabItem create(Context context) {
        return (CourseDetailTabItem) LayoutInflater.from(context).inflate(R.layout.layout_course_detail_tab_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tabLeftTv = (TextView) findViewById(R.id.tab_left);
        tabCenterTv = (TextView) findViewById(R.id.tab_center);
        tabRightTv = (TextView) findViewById(R.id.tab_right);
        tabBottomView = findViewById(R.id.tab_bottom_shape);

        tabLeftTv.setOnClickListener(this);
        tabCenterTv.setOnClickListener(this);
        tabRightTv.setOnClickListener(this);

        LayoutParams lp = (LayoutParams) tabBottomView.getLayoutParams();
        lp.width = Enviroment.screenWidth(getContext()) / 3;
        tabBottomView.setLayoutParams(lp);
    }

    public void setData(String left, String center, String right) {
        tabLeftTv.setText(left);
        tabCenterTv.setText(center);
        tabRightTv.setText(right);
    }

    public void setListener(OnTabChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tab_left) {
            setIndex(0);

            if (listener != null) {
                listener.onTabChanged(this, 0);
            }

        } else if (v.getId() == R.id.tab_center) {
            setIndex(1);

            if (listener != null) {
                listener.onTabChanged(this, 1);
            }

        } else if (v.getId() == R.id.tab_right) {
            setIndex(2);

            if (listener != null) {
                listener.onTabChanged(this, 2);
            }
        }
    }

    public void setIndex(int index) {
        if (index == 0) {
            LayoutParams lp = (LayoutParams) tabBottomView.getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
            tabBottomView.setLayoutParams(lp);

            tabLeftTv.setTextColor(getResources().getColor(R.color.text_blue));
            tabCenterTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            tabRightTv.setTextColor(getResources().getColor(R.color.text_deep_gray));

        } else if (index == 1) {
            LayoutParams lp = (LayoutParams) tabBottomView.getLayoutParams();
            lp.setMargins(lp.width, 0, 0, 0);
            tabBottomView.setLayoutParams(lp);

            tabLeftTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            tabCenterTv.setTextColor(getResources().getColor(R.color.text_blue));
            tabRightTv.setTextColor(getResources().getColor(R.color.text_deep_gray));

        } else {
            LayoutParams lp = (LayoutParams) tabBottomView.getLayoutParams();
            lp.setMargins(lp.width * 2, 0, 0, 0);
            tabBottomView.setLayoutParams(lp);

            tabLeftTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            tabCenterTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            tabRightTv.setTextColor(getResources().getColor(R.color.text_blue));
        }
    }

    public interface OnTabChangeListener {
        void onTabChanged(CourseDetailTabItem tabItem, int index);
    }

}
