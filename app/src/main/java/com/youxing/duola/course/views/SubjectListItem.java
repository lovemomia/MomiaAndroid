package com.youxing.duola.course.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.BookingSubjectListModel;
import com.youxing.duola.model.Order;
import com.youxing.duola.utils.PriceUtils;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class SubjectListItem extends LinearLayout {

    private YXNetworkImageView iconIv;
    private TextView titleTv;
    private TextView dateTv;
    private TextView timesTv;

    public SubjectListItem(Context context) {
        super(context);
    }

    public SubjectListItem(Context context, AttributeSet attrs) {
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

    public static SubjectListItem create(Context context) {
        return (SubjectListItem) LayoutInflater.from(context).inflate(R.layout.layout_subject_list_item, null);
    }

    public void setData(BookingSubjectListModel.BookingSubject subject) {
        iconIv.setImageUrl(subject.getCover());
        titleTv.setText(subject.getTitle());
        dateTv.setText(subject.getExpireTime());
        timesTv.setText("还可约" + subject.getBookableCourseCount() + "次课");
    }

}
