package com.youxing.momia.order;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.momia.R;
import com.youxing.momia.app.TQActivity;

/**
 * 提交订单页
 *
 * Created by Jun Deng on 15/6/8.
 */
public class OrderSubmitActivity extends TQActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_submit);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new Adapter());

    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(OrderSubmitActivity.this);
        }

        @Override
        public int getCountInSection(int section) {
            return 0;
        }

        @Override
        public int getSectionCount() {
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            return null;
        }

    }
}
