package com.youxing.duola.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.youxing.duola.R;
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.views.ViewPagerIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jun Deng on 15/8/28.
 */
public class ProductCalendarActivity extends DLActivity {

    private ViewPagerIndicatorView viewPagerIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tab);

        this.viewPagerIndicatorView = (ViewPagerIndicatorView) findViewById(R.id.viewpager_indicator_view);
        List<String> titleList = new ArrayList<>();

        String tab1 = "周末";
        titleList.add(tab1);

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);

        String tab2 = getMonthChinese(month) + "月";
        titleList.add(tab2);

        String tab3 = getMonthChinese(month + 1) + "月";
        titleList.add(tab3);


        final Map<String, Fragment> map = new HashMap();
        map.put(tab3, createMonthFragment(month + 2));
        map.put(tab2, createMonthFragment(month + 1));
        map.put(tab1, createWeekendFragment());
        this.viewPagerIndicatorView.setupFragment(titleList, map);

    }

    public String getMonthChinese(int month) {
        switch (month) {
            case 0:
                return "一";
            case 1:
                return "二";
            case 2:
                return "三";
            case 3:
                return "四";
            case 4:
                return "五";
            case 5:
                return "六";
            case 6:
                return "七";
            case 7:
                return "八";
            case 8:
                return "九";
            case 9:
                return "十";
            case 10:
                return "十一";
            case 11:
                return "十二";
            default:
                return "一";
        }
    }

    private Fragment createMonthFragment(int month) {
        MonthProductListFragment fragment = new MonthProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("month", month);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Fragment createWeekendFragment() {
        WeekendProductListFragment fragment = new WeekendProductListFragment();
        return fragment;
    }
}
