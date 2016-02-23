package com.youxing.duola.course.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jun Deng on 16/2/23.
 */
public class BookSkuListDateItem extends LinearLayout {

    private TextView dayTv;
    private TextView monthTv;
    private TextView weekTv;

    public BookSkuListDateItem(Context context) {
        super(context);
    }

    public BookSkuListDateItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dayTv = (TextView) findViewById(R.id.day);
        monthTv = (TextView) findViewById(R.id.month);
        weekTv = (TextView) findViewById(R.id.week);
    }

    public static BookSkuListDateItem create(Context context) {
        return (BookSkuListDateItem) LayoutInflater.from(context).inflate(R.layout.layout_book_sku_date_list_item, null);
    }

    public void setData(String dateStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(dateStr);
        } catch (Exception e) {
            return ;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dayTv.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        monthTv.setText(getMonth(calendar.get(Calendar.MONTH)) + "月");
        weekTv.setText(getWeek(calendar.get(Calendar.DAY_OF_WEEK)));
    }

    public int getMonth(int month) {
        int monthIndex = month + 1;
        if (monthIndex > 12) {
            monthIndex = 1;
        }
        return monthIndex;
    }

    public String getWeek(int week) {
        if (week == Calendar.SUNDAY) {
            return "星期天";
        } else if (week == Calendar.MONDAY) {
            return "星期一";
        } else if (week == Calendar.TUESDAY) {
            return "星期二";
        } else if (week == Calendar.WEDNESDAY) {
            return "星期三";
        } else if (week == Calendar.THURSDAY) {
            return "星期四";
        } else if (week == Calendar.FRIDAY) {
            return "星期五";
        } else {
            return "星期六";
        }
    }
}
