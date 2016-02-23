package com.youxing.duola.course;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.views.ViewPagerIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jun Deng on 16/2/22.
 */
public class BookActivity extends SGActivity {

    private String id;
    private boolean onlyshow;

    private ViewPagerIndicatorView viewPagerIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tab);

        id = getIntent().getData().getQueryParameter("id");
        String onlyshowStr = getIntent().getData().getQueryParameter("onlyshow");
        if (!TextUtils.isEmpty(onlyshowStr)) {
            onlyshow = Boolean.valueOf(onlyshowStr);
        }

        this.viewPagerIndicatorView = (ViewPagerIndicatorView) findViewById(R.id.viewpager_indicator_view);
        List<String> titleList = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        String tab1 = getMonthChinese(month) + "月";
        titleList.add(tab1);
        String tab2 = getMonthChinese(month + 1) + "月";
        titleList.add(tab2);
        final Map<String, Fragment> map = new HashMap();
        map.put(tab1, createFragment(month + 1));
        map.put(tab2, createFragment(month + 2));
        this.viewPagerIndicatorView.setupFragment(titleList, map);
    }

    private Fragment createFragment(int month) {
        BookSkuListFragment fragment = new BookSkuListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("month", month);
        bundle.putBoolean("onlyshow", onlyshow);
        fragment.setArguments(bundle);
        return fragment;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 0, "提交").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
}
