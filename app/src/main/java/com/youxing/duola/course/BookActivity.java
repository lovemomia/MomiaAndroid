package com.youxing.duola.course;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.CourseSkuListModel;
import com.youxing.duola.views.ViewPagerIndicatorView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jun Deng on 16/2/22.
 */
public class BookActivity extends SGActivity implements ViewPagerIndicatorView.OnTabChangedListener {

    private static final int MENU_ID_SUBMIT = 1;

    private String id;
    private String pid;
    private int onlyshow;

    private ViewPagerIndicatorView viewPagerIndicatorView;
    private BookSkuListFragment currentMonthFragment;
    private BookSkuListFragment nextMonthFragment;

    private CourseSkuListModel.CourseSku selectSku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tab);

        id = getIntent().getData().getQueryParameter("id");
        pid = getIntent().getData().getQueryParameter("pid");
        String onlyshowStr = getIntent().getData().getQueryParameter("onlyshow");
        if (!TextUtils.isEmpty(onlyshowStr)) {
            onlyshow = Integer.valueOf(onlyshowStr);
        }

        if (onlyshow == 1) {
            setTitle("课程表");
        } else {
            setTitle("预约课程");
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
        currentMonthFragment = createFragment(month + 1);
        nextMonthFragment = createFragment(month + 2);
        map.put(tab1, currentMonthFragment);
        map.put(tab2, nextMonthFragment);

        this.viewPagerIndicatorView.setupFragment(titleList, map);
        viewPagerIndicatorView.setOnTabChangeListener(this);
    }

    @Override
    public void onTabChanged(int tab) {
        if (tab == 0) {
            currentMonthFragment.refresh();
        } else {
            nextMonthFragment.refresh();
        }
    }

    private BookSkuListFragment createFragment(int month) {
        BookSkuListFragment fragment = new BookSkuListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("month", month);
        bundle.putBoolean("onlyshow", onlyshow == 1);
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

    public void setSelectSku(CourseSkuListModel.CourseSku selectSku) {
        this.selectSku = selectSku;
    }

    public CourseSkuListModel.CourseSku getSelectSku() {
        return selectSku;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (onlyshow != 1) {
            menu.add(1, MENU_ID_SUBMIT, 0, "提交").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ID_SUBMIT) {
            if (selectSku == null) {
                showDialog(BookActivity.this, "您还未选择场次");
                return true;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("duola://bookconfirm?pid=" + pid));
            intent.putExtra("sku", selectSku);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
