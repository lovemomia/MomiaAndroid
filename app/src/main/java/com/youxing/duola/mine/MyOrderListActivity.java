package com.youxing.duola.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.views.ViewPagerIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderListActivity extends SGActivity {

    private ViewPagerIndicatorView viewPagerIndicatorView;

    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tab);

        this.viewPagerIndicatorView = (ViewPagerIndicatorView) findViewById(R.id.viewpager_indicator_view);
        List<String> titleList = new ArrayList<String>();
        titleList.add("待付款");
        titleList.add("已付款");
        titleList.add("全部");
        final Map<String, Fragment> map = new HashMap();
        map.put("已付款", createFragment(3));
        map.put("待付款", createFragment(2));
        map.put("全部", createFragment(1));
        this.viewPagerIndicatorView.setupFragment(titleList, map);

        String statusStr = getIntent().getData().getQueryParameter("status");
        if (!TextUtils.isEmpty(statusStr)) {
            status = Integer.valueOf(statusStr);
        }

        if (status == 3) {
            this.viewPagerIndicatorView.setCurrentPage(1);
        }
    }

    private Fragment createFragment(int status) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

}
