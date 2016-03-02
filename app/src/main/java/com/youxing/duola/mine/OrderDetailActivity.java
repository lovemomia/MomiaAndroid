package com.youxing.duola.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.mine.views.OrderDetailInfoView;
import com.youxing.duola.mine.views.OrderListItem;
import com.youxing.duola.model.OrderDetailModel;
import com.youxing.duola.utils.PriceUtils;
import com.youxing.duola.views.ProductListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class OrderDetailActivity extends SGActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private String oid;

    private ListView listView;
    private Adapter adapter;

    private OrderDetailModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        oid = getIntent().getData().getQueryParameter("oid");

        requestData();
    }

    private void requestData() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("oid", oid));
        HttpService.get(Constants.domain() + "/subject/order/detail", params, CacheType.DISABLE, OrderDetailModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = (OrderDetailModel) response;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(OrderDetailActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.section == 0) {
            if (model.getData().getCourseId() > 0) {
                startActivity("duola://coursedetail?id=" + model.getData().getCourseId());

            } else {
                startActivity("duola://subjectdetail?id=" + model.getData().getSubjectId());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (model.getData().getStatus() == 2) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://cashpay?order=" + JSON.toJSONString(model.getData()))));

        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://bookingsubjectlist?oid=" + model.getData().getId())));
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(OrderDetailActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (model != null) {
                return 3;
            }
            return 0;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 1;
            } else if (section == 1) {
                if (!TextUtils.isEmpty(model.getData().getCouponDesc())) {
                    return 6;
                }
                return 5;
            }
            return 0;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            int status = model.getData().getStatus();
            int bookStatus = model.getData().getBookingStatus();
            if (section == 2 && (status == 2 || bookStatus == 1)) {
                LinearLayout ll = new LinearLayout(OrderDetailActivity.this);
                int padding = UnitTools.dip2px(OrderDetailActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(OrderDetailActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText(status == 2 ? "继续支付" : "预约课程");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(OrderDetailActivity.this);
                padding = UnitTools.dip2px(OrderDetailActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_red);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }

        @Override
        public int getHeightForSectionView(int section) {
            if (section == 0) {
                return 0;
            } else if (section == 3) {
                return 60;
            }
            return super.getHeightForSectionView(section);
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0) {
                OrderListItem item = OrderListItem.create(OrderDetailActivity.this);
                item.setData(model.getData(), false);
                view = item;

            } else {
                TextView tv = new TextView(OrderDetailActivity.this);
                tv.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                        AbsListView.LayoutParams.WRAP_CONTENT));
                int padding = UnitTools.dip2px(OrderDetailActivity.this, 10);
                tv.setPadding(padding, padding, padding, padding);
                tv.setTextColor(row == 0 ? getResources().getColor(R.color.text_deep_gray) :
                        getResources().getColor(R.color.text_gray));
                tv.setTextSize(14);

                if (row == 0) {
                    tv.setText("订单信息");

                } else if (row == 1) {
                    tv.setText("订单号：" + model.getData().getId());

                } else if (row == 2) {
                    tv.setText("数量：" + model.getData().getCount());

                } else if (row == 3) {
                    tv.setText("总价：" + model.getData().getTotalFee());

                } else if (row == 4 && !TextUtils.isEmpty(model.getData().getCouponDesc())) {
                    tv.setText("使用抵扣：" + model.getData().getCouponDesc());

                } else {
                    tv.setText("下单时间：" + model.getData().getAddTime());
                }

                view = tv;
            }
            return view;
        }
    }

}
