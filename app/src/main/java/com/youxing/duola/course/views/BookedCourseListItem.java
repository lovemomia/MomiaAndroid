package com.youxing.duola.course.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.Course;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/2/25.
 */
public class BookedCourseListItem extends CourseListItem implements View.OnClickListener {

    private Button eventBtn;

    private Course course;
    private boolean finish;

    private OnCourseCancelBookListener cancelBookListener;

    public BookedCourseListItem(Context context) {
        super(context);
    }

    public BookedCourseListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static BookedCourseListItem create(Context context) {
        return (BookedCourseListItem) LayoutInflater.from(context).inflate(R.layout.layout_booked_course_list_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        eventBtn = (Button) findViewById(R.id.event);
        eventBtn.setOnClickListener(this);
    }

    public void setData(Course course, boolean finish) {
        this.course = course;
        this.finish = finish;

        iconIv.setImageUrl(course.getCover());
        titleTv.setText(course.getTitle());
        dateTv.setText(course.getScheduler());
        if (course.getPlace() != null && !TextUtils.isEmpty(course.getPlace().getName())) {
            timesTv.setText(course.getPlace().getName());
        } else {
            timesTv.setText(course.getRegion());
        }

        if (finish) {
            eventBtn.setText("评价");
            eventBtn.setBackgroundResource(R.drawable.btn_shape_red);
        } else {
            eventBtn.setText("取消预约");
            eventBtn.setBackgroundResource(R.drawable.btn_shape_green);
        }
    }

    @Override
    public void onClick(View v) {
        if (course == null) {
            return;
        }

        if (finish) {
            goComment();

        } else {
            cancelBook();
        }
    }

    private SGActivity getSGActivity() {
        return (SGActivity)getContext();
    }

    public void setCancelBookListener(OnCourseCancelBookListener cancelBookListener) {
        this.cancelBookListener = cancelBookListener;
    }

    private void cancelBook() {
        AlertDialog dlg = new AlertDialog.Builder(getContext()).create();
        dlg.setMessage("确认取消预约吗？");
        dlg.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSGActivity().showLoadingDialog(getContext());
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("bid", String.valueOf(course.getBookingId())));
                HttpService.post(Constants.domain() + "/course/cancel", params, BaseModel.class, new RequestHandler() {
                    @Override
                    public void onRequestFinish(Object response) {
                        getSGActivity().dismissDialog();
                        if (cancelBookListener != null) {
                            cancelBookListener.onCourseCancelBook(course);
                        }
                    }

                    @Override
                    public void onRequestFailed(BaseModel error) {
                        getSGActivity().showDialog(getContext(), error.getErrmsg());
                    }
                });
            }
        });
        dlg.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dlg.show();
    }

    private void goComment() {

    }

    public interface OnCourseCancelBookListener {
        void onCourseCancelBook(Course course);
    }

}
