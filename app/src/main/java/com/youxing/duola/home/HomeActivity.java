package com.youxing.duola.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.NetModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.home.views.HomeHeaderView;
import com.youxing.duola.home.views.HomeListItem;
import com.youxing.duola.model.HomeModel;
import com.youxing.duola.model.Product;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/6/8.
 */
public class HomeActivity extends DLActivity implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, RequestHandler {

    private SwipeRefreshLayout swipeLayout;
    private Adapter adapter;

    private List<Product> productList = new ArrayList<>();
    private int nextPage;

    private boolean isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


        ListView listView = (ListView)findViewById(R.id.listView);
        listView.addHeaderView(createHeaderView());
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        initTitle();

        requestData();
    }

    private void initTitle() {
        setTitle("上海");
        Drawable img = getResources().getDrawable(R.drawable.ic_arrow_down);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        titleTv.setCompoundDrawables(null, null, img, null);
        titleTv.setCompoundDrawablePadding(10);
        titleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setTitleLeftButton(R.drawable.ic_action_mine, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setTitleRightButton(R.drawable.ic_action_search, new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void requestData() {
        showLoading();

        String url = Constants.DOMAIN_ONLINE + "/home";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("pageindex", String.valueOf(nextPage)));

        HttpService.get(url, params, CacheType.NORMAL, HomeModel.class, this);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
    }

    @Override
    public boolean isDarkTitleBar() {
        return true;
    }

    private HomeHeaderView createHeaderView() {
        HomeHeaderView header = HomeHeaderView.create(this);
        header.setData();
        return header;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://product")));
    }

    @Override
    public void onRequestFinish(NetModel response) {
        dismissLoading();
        if (isRefresh) {
            isRefresh = false;
            swipeLayout.setRefreshing(false);
        }

        HomeModel homeModel = (HomeModel) response;
        productList.addAll(homeModel.getData().getProducts());
//        nextPage = homeModel.getData().getNextpage();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestFailed(NetModel error) {
        dismissLoading();
        if (isRefresh) {
            isRefresh = false;
            swipeLayout.setRefreshing(false);
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(HomeActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (nextPage > 0) {
                return productList.size() + 1;
            }
            return productList.size();
        }

        @Override
        public int getCountInSection(int section) {
            if (nextPage > 0 && section == getSectionCount() - 1) {
                return 0;
            }
            return 1;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = convertView;
            if (!(view instanceof HomeListItem)) {
                view = HomeListItem.create(HomeActivity.this);
            }
            ((HomeListItem) view).setData();
            return view;
        }

        @Override
        public int getHeightForSectionView(int section) {
            return 15;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (nextPage > 0 && section == getSectionCount() - 1) {
                return getLoadingView(parent, convertView);
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }

    @Override
    protected void onDestroy() {
        HttpService.abort(this);
        super.onDestroy();
    }
}
