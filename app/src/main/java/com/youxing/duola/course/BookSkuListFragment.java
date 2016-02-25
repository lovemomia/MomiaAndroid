package com.youxing.duola.course;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.youxing.duola.app.SGFragment;
import com.youxing.duola.course.views.BookSkuListDateItem;
import com.youxing.duola.course.views.BookSkuListItem;
import com.youxing.duola.model.CourseSkuListModel;
import com.youxing.duola.views.EmptyView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/2/23.
 */
public class BookSkuListFragment extends SGFragment implements AdapterView.OnItemClickListener {

    private String id;
    private int month;
    private boolean onlyshow;

    private Adapter adapter;

    private CourseSkuListModel model;
    private boolean isEmpty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getString("id");
        month = getArguments().getInt("month");
        onlyshow = getArguments().getBoolean("onlyshow");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, null);
        ListView listView = (ListView)view.findViewById(R.id.listView);
        adapter = new Adapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestData();
    }

    public void refresh() {
        adapter.notifyDataSetChanged();
    }

    private void requestData() {
        getDLActivity().showLoadingDialog(getActivity());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("month", String.valueOf(month)));

        String path = onlyshow ? "/course/sku/month/notend" : "/course/sku/month/bookable";

        HttpService.get(Constants.domain() + path, params, CacheType.DISABLE, CourseSkuListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                getDLActivity().dismissDialog();

                model = (CourseSkuListModel) response;
                if (model.getData().size() == 0) {
                    isEmpty = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                getDLActivity().showDialog(getActivity(), error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isEmpty) {
            return;
        }

        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.row == 0) {
            return;
        }

        CourseSkuListModel.DateSkuList skuList = model.getData().get(indexPath.section);
        ((BookActivity)getActivity()).setSelectSku(skuList.getSkus().get(indexPath.row - 1));
        adapter.notifyDataSetChanged();
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            if (model != null) {
                if (isEmpty) {
                    return 1;
                }
                return model.getData().size();
            }
            return 0;
        }

        @Override
        public int getCountInSection(int section) {
            if (isEmpty) {
                return 0;
            }
            CourseSkuListModel.DateSkuList skuList = model.getData().get(section);
            return skuList.getSkus().size() + 1;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            CourseSkuListModel.DateSkuList skuList = model.getData().get(section);
            if (row == 0) {
                BookSkuListDateItem listItem = BookSkuListDateItem.create(getActivity());
                listItem.setData(skuList.getDate());
                return listItem;

            } else {
                BookSkuListItem listItem = BookSkuListItem.create(getActivity());
                CourseSkuListModel.CourseSku sku = skuList.getSkus().get(row - 1);
                listItem.setData(sku);

                CourseSkuListModel.CourseSku selectSku = ((BookActivity)getActivity()).getSelectSku();
                if (selectSku != null && sku.getId() == selectSku.getId()) {
                    listItem.setSelect(true);
                } else {
                    listItem.setSelect(false);
                }

                return listItem;
            }
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (isEmpty) {
                EmptyView emptyView = EmptyView.create(getActivity());
                emptyView.setMessage("还没有课程，敬请期待哦～");
                return emptyView;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }
}
