package com.youxing.duola.course;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.CourseSkuListModel;
import com.youxing.duola.views.ViewPagerIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jun Deng on 16/2/25.
 */
public class BookedCourseListActivity extends SGActivity {

    private String id; // course id

    private ViewPagerIndicatorView viewPagerIndicatorView;

    private CourseSkuListModel.CourseSku selectSku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tab);

        id = getIntent().getData().getQueryParameter("id");

        this.viewPagerIndicatorView = (ViewPagerIndicatorView) findViewById(R.id.viewpager_indicator_view);
        List<String> titleList = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        titleList.add("待上课");
        titleList.add("已上课");

        final Map<String, Fragment> map = new HashMap();
        map.put("待上课", createFragment(false));
        map.put("已上课", createFragment(true));

        this.viewPagerIndicatorView.setupFragment(titleList, map);
    }

    private Fragment createFragment(boolean finish) {
        BookedCourseListFragment fragment = new BookedCourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putBoolean("finish", finish);
        fragment.setArguments(bundle);
        return fragment;
    }

}
