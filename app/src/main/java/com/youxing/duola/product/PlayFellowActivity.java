package com.youxing.duola.product;

import android.os.Bundle;
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
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.model.PlayFellowModel;
import com.youxing.duola.product.views.PlayFellowItemView;
import com.youxing.duola.product.views.PlayFellowSectionView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/28.
 */
public class PlayFellowActivity extends DLActivity implements AdapterView.OnItemClickListener {

    private Adapter adapter;

    private PlayFellowModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        requestData();
    }

    private void requestData() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", getIntent().getData().getQueryParameter("id")));

        HttpService.get(Constants.domain() + "/product/playmate", params, CacheType.DISABLE, PlayFellowModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = (PlayFellowModel) response;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(PlayFellowActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.row == -1) {
            PlayFellowModel.PlayFellowGroup group = model.getData().get(indexPath.section);
            group.setSelected(!group.isSelected());
            adapter.notifyDataSetChanged();
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(PlayFellowActivity.this);
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public int getSectionCount() {
            if (model == null) {
                return 0;
            }
            return model.getData().size();
        }

        @Override
        public int getCountInSection(int section) {
            PlayFellowModel.PlayFellowGroup group = model.getData().get(section);
            if (!group.isSelected()) {
                return 0;
            }

            if (model.getData().get(section).getPlaymates() == null
                    || model.getData().get(section).getPlaymates().size() == 0) {
                return 1;
            }
            return model.getData().get(section).getPlaymates().size();
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view;
            if (model.getData().get(section).getPlaymates() == null
                    || model.getData().get(section).getPlaymates().size() == 0) {
                view = getEmptyView("该场次暂时还没有人报名哦~", parent, convertView);
            } else {
                PlayFellowItemView itemView = PlayFellowItemView.create(PlayFellowActivity.this);
                itemView.setData(model.getData().get(section).getPlaymates().get(row));
                view = itemView;
            }
            return view;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            PlayFellowSectionView sectionView = PlayFellowSectionView.create(PlayFellowActivity.this);
            sectionView.setData(model.getData().get(section));
            return sectionView;
        }

        @Override
        public int getBackgroundColorForRow(IndexPath indexPath) {
            return getResources().getColor(R.color.bg_window);
        }
    }
}
