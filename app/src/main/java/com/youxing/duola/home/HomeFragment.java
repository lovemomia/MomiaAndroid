package com.youxing.duola.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;
import com.youxing.duola.app.DLFragment;
import com.youxing.duola.home.views.HomeHeaderView;
import com.youxing.duola.home.views.HomeListItem;
import com.youxing.duola.model.HomeModel;
import com.youxing.duola.model.Product;
import com.youxing.duola.views.TitleBar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 *
 * Created by Jun Deng on 15/8/3.
 */
public class HomeFragment extends DLFragment implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, RequestHandler {

    private View rootView;
    private boolean rebuild;

    private SwipeRefreshLayout swipeLayout;
    private TitleBar titleBar;
    private ListView listView;
    private Adapter adapter;

    private List<Product> productList = new ArrayList<>();
    private int nextPage;

    private boolean isRefresh;
    private boolean hasBannel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_home, null);

            titleBar = (TitleBar) rootView.findViewById(R.id.titleBar);
            listView = (ListView)rootView.findViewById(R.id.listView);
            listView.setOnItemClickListener(this);

            swipeLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                    android.R.color.holo_orange_light, android.R.color.holo_red_light);

            rebuild = true;
        } else {
            rebuild = false;
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (rebuild) {
            initTitle();

            requestData();
        }
    }

    private void initTitle() {
        titleBar.getTitleTv().setText("哆啦亲子");

        titleBar.getLeftBtn().setText("上海");
        Drawable img = getResources().getDrawable(R.drawable.ic_arrow_down);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        titleBar.getLeftBtn().getTextView().setCompoundDrawables(null, null, img, null);
        titleBar.getLeftBtn().getTextView().setCompoundDrawablePadding(10);
        titleBar.getLeftBtn().getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void requestData() {
        if (nextPage == 0) {
            getDLActivity().showLoading();
        }

        String url = Constants.domain() + "/home";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("pageindex", String.valueOf(nextPage)));

        HttpService.get(url, params, CacheType.NORMAL, HomeModel.class, this);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        nextPage = 0;
        productList.clear();
        requestData();
    }

    private HomeHeaderView createHeaderView(List<HomeModel.HomeBanner> banners) {
        HomeHeaderView header = HomeHeaderView.create(getActivity());
        header.setData(banners);
        return header;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int section = adapter.getIndexForPosition(hasBannel ? position - 1 : position).section;
        Product product = productList.get(section);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://productdetail?id=" + product.getId())));
    }

    @Override
    public void onRequestFinish(BaseModel response) {
        if (isRefresh) {
            isRefresh = false;
            swipeLayout.setRefreshing(false);
        }

        HomeModel homeModel = (HomeModel) response;
        productList.addAll(homeModel.getData().getProducts());

        if (nextPage == 0) {
            getDLActivity().dismissLoading();

            if (homeModel.getData().getBanners() != null && homeModel.getData().getBanners().size() > 0) {
                listView.addHeaderView(createHeaderView(homeModel.getData().getBanners()));
                hasBannel = true;
            }

            adapter = new Adapter();
            listView.setAdapter(adapter);
        } else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
        nextPage = homeModel.getData().getNextpage();
    }

    @Override
    public void onRequestFailed(BaseModel error) {
        getDLActivity().dismissLoading();
        if (isRefresh) {
            isRefresh = false;
            swipeLayout.setRefreshing(false);
        }
        getDLActivity().showDialog(getDLActivity(), "对不起", error.getErrmsg(), "确定");
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(getDLActivity());
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
                view = HomeListItem.create(getDLActivity());
            }
            ((HomeListItem) view).setData(productList.get(section));
            return view;
        }

        @Override
        public int getBackgroundColorForRow(IndexPath indexPath) {
            return Color.TRANSPARENT;
        }

        @Override
        public int getHeightForSectionView(int section) {
            return UnitTools.dip2px(getActivity(), 10);
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (nextPage > 0 && section == getSectionCount() - 1) {
                requestData();
                return getLoadingView(parent, convertView);
            }
            View view = super.getViewForSection(convertView, parent, section);
            view.setBackgroundResource(R.color.bg_window);
            return view;
        }
    }

    @Override
    public void onDestroy() {
        HttpService.abort(this);
        super.onDestroy();
    }

}
