package com.youxing.duola.home;

import android.os.Bundle;
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
import com.youxing.duola.R;
import com.youxing.duola.app.DLFragment;
import com.youxing.duola.home.views.ProductCalendarHeader;
import com.youxing.duola.model.Product;
import com.youxing.duola.model.ProductCalendarMonthModel;
import com.youxing.duola.views.EmptyView;
import com.youxing.duola.views.ProductListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/28.
 */
public class MonthProductListFragment extends DLFragment implements AdapterView.OnItemClickListener {

    private View rootView;
    private boolean rebuild;
    private ListView listView;
    private Adapter adapter;

    private List<ProductCalendarMonthModel.ProductCalendarGroup> groups = new ArrayList<ProductCalendarMonthModel.ProductCalendarGroup>();
    private boolean isEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_list, null);
            listView = (ListView)rootView.findViewById(R.id.listView);
            adapter = new Adapter();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
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
        requestData();
    }

    private void requestData() {
        getDLActivity().showLoadingDialog(getActivity());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("month", String.valueOf(getArguments().getInt("month"))));
        HttpService.get(Constants.domain() + "/product/month", params, CacheType.DISABLE, ProductCalendarMonthModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(BaseModel response) {
                getDLActivity().dismissDialog();
                ProductCalendarMonthModel model = (ProductCalendarMonthModel) response;
                groups.addAll(model.getData());
                if (groups.size() == 0) {
                    isEmpty = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                getDLActivity().dismissDialog();
                getDLActivity().showDialog(getDLActivity(), error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.row > 0) {
            Product product = groups.get(indexPath.section).getProducts().get(indexPath.row - 1);
            startActivity("duola://productdetail?id=" + product.getId());
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(getActivity());
        }

        @Override
        public int getSectionCount() {
            if (isEmpty) {
                return 1;
            }
            return groups.size();
        }

        @Override
        public int getCountInSection(int section) {
            if (isEmpty) {
                return 0;
            }
            return groups.get(section).getProducts().size() + 1;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (row == 0) {
                String date = groups.get(section).getDate();
                ProductCalendarHeader header = ProductCalendarHeader.create(getActivity());
                header.setData(date);
                view = header;

            } else {
                Product product = groups.get(section).getProducts().get(row - 1);
                ProductListItem couponListItem = ProductListItem.create(getDLActivity());
                couponListItem.setData(product);
                view = couponListItem;
            }
            return view;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (isEmpty) {
                String msg = "还没有活动，敬请期待哦~";
                EmptyView emptyView = EmptyView.create(getDLActivity());
                emptyView.setMessage(msg);
                return emptyView;
            }
            return super.getViewForSection(convertView, parent, section);
        }

    }

}
