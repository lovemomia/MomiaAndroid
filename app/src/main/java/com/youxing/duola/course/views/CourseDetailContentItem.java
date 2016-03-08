package com.youxing.duola.course.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Course;

/**
 * Created by Jun Deng on 16/3/7.
 */
public class CourseDetailContentItem extends LinearLayout {

    public CourseDetailContentItem(Context context) {
        this(context, null);
    }

    public CourseDetailContentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }

    public static CourseDetailContentItem create(Context context) {
        return new CourseDetailContentItem(context);
    }

    public void setData(Course course) {
        int padding = UnitTools.dip2px(getContext(), 10);
        this.setPadding(padding, padding, padding, padding);

        // goal
        TextView goalTv = new TextView(getContext());
        goalTv.setText(course.getGoal());
        addView(goalTv);

        for (int i = 0; i < course.getDetail().size(); i++) {
            Course.CourseDetail detail = course.getDetail().get(i);

            addView(createIndexTitle(i + 1, detail.getTitle()));

            for (Course.CourseDetailContent content : detail.getContent()) {
                // text
                if (!TextUtils.isEmpty(content.getText())) {
                    TextView contentTv = new TextView(getContext());
                    contentTv.setPadding(0, 0, 0, padding);
                    contentTv.setText(content.getText());
                    addView(contentTv);
                }

                // img
                if (!TextUtils.isEmpty(content.getImg())) {
                    YXNetworkImageView imageView = new YXNetworkImageView(getContext());
                    int width = Enviroment.screenWidth(getContext()) - 2 * padding;
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(width, width * 225 / 320));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setDefaultImageResId(R.drawable.bg_default_image);
                    imageView.setImageUrl(content.getImg());
                    addView(imageView);
                }
            }
        }


    }

    private View createIndexTitle(int index, String title) {
        LinearLayout layout = new LinearLayout(getContext());
        int padding = UnitTools.dip2px(getContext(), 10);
        layout.setPadding(0, padding, 0, padding);

        TextView indexTv = new TextView(getContext());
        indexTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
        indexTv.setTextSize(14);
        indexTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        indexTv.setText(String.valueOf(index));
        layout.addView(indexTv);

        TextView titleTv = new TextView(getContext());
        titleTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
        titleTv.setTextSize(14);
        titleTv.setText(title);
        layout.addView(titleTv);

        return layout;
    }

}
