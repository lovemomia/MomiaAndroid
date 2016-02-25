package com.youxing.duola.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.views.ViewPagerIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderListActivity extends SGActivity {

    private ViewPagerIndicatorView viewPagerIndicatorView;

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

    }

    private Fragment createFragment(int status) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

}
