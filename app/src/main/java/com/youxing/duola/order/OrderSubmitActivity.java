package com.youxing.duola.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.duola.R;
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.views.SectionView;

/**
 * 提交订单页
 *
 * Created by Jun Deng on 15/6/8.
 */
public class OrderSubmitActivity extends DLActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_SELECT_PERSON = 1;

    private ListView listView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_submit);
        listView = (ListView)findViewById(R.id.listView);
        listView.addFooterView(createFooterView());
        adapter = new Adapter();
        listView.setAdapter(adapter);
        findViewById(R.id.done).setOnClickListener(this);
    }

    private View createFooterView() {
        SectionView view = SectionView.create(this);
        view.setTitle("我已阅读并同意预订须知和重要条款");
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("duola://tripperson")), REQUEST_CODE_SELECT_PERSON);

        } else if (v.getId() == R.id.done) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://cashier")));
        }

    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(OrderSubmitActivity.this);
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 1) {
                return 2;
            }
            return 1;
        }

        @Override
        public int getSectionCount() {
            return 3;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0) {
                view = inflateView(R.layout.layout_list_item_select);
                view.findViewById(R.id.add).setOnClickListener(OrderSubmitActivity.this);

            } else if (section == 1) {
                view = inflateView(R.layout.layout_list_item_input);

            } else if (section == 2) {
                view = inflateView(R.layout.layout_list_item_input);
            }
            return view;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            SectionView view = SectionView.create(OrderSubmitActivity.this);
            if (section == 0) {
                view.setTitle("出行人");
            } else if (section == 1) {
                view.setTitle("联系人信息");
            } else if (section == 2) {
                view.setTitle("备注");
            }
            return view;
        }
    }
}
