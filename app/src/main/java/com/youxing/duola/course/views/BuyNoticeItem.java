package com.youxing.duola.course.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/3/7.
 */
public class BuyNoticeItem extends LinearLayout {

    public BuyNoticeItem(Context context) {
        this(context, null);
    }

    public BuyNoticeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        int padding = UnitTools.dip2px(getContext(), 10);
        setPadding(padding, padding, padding, padding);
    }

    public static BuyNoticeItem create(Context context) {
        return new BuyNoticeItem(context);
    }

    public void setData(String notice) {
        List<Notice> notices = new ArrayList<Notice>(JSONArray.parseArray(notice, Notice.class));

        removeAllViews();
        for (Notice n : notices) {

            TextView titleTv = new TextView(getContext());
            titleTv.setText(n.getTitle());
            titleTv.setTextSize(14);
            titleTv.setTextColor(getResources().getColor(R.color.text_blue));
            titleTv.setPadding(0, 0, 0, UnitTools.dip2px(getContext(), 5));
            addView(titleTv);

            TextView contentTv = new TextView(getContext());
            contentTv.setText(n.getContent());
            contentTv.setTextSize(14);
            contentTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            contentTv.setLineSpacing(3, 1.2f);
            contentTv.setPadding(0, 0, 0, UnitTools.dip2px(getContext(), 10));
            addView(contentTv);
        }
    }

    private static class Notice {
        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
