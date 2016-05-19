package com.youxing.duola.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.Order;
import com.youxing.duola.model.PostOrderModel;
import com.youxing.duola.order.views.ApplyRefundCouseItem;
import com.youxing.duola.utils.PriceUtils;
import com.youxing.duola.views.SectionView;
import com.youxing.duola.views.SimpleListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请退款
 *
 * Created by Jun Deng on 16/4/28.
 */
public class ApplyRefundActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private Order order;

    private Adapter adapter;

    private static final String[] couses = {"买多了/买错了", "计划有变，没时间上课", "后悔了，不想要了", "预约不到想上的课", "其他原因"};
    private int couseIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        order = getIntent().getParcelableExtra("order");

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        HttpService.abort(submitHandler);
        super.onDestroy();
    }

    private RequestHandler submitHandler = new RequestHandler() {
        @Override
        public void onRequestFinish(Object response) {
            dismissDialog();

            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void onRequestFailed(BaseModel error) {
            dismissDialog();
            showDialog(ApplyRefundActivity.this, error.getErrmsg());
        }
    };

    private void requestSubmit() {
        if (couseIndex < 0) {
            showDialog(ApplyRefundActivity.this, "您还没有选择退款原因");
            return;
        }

        showLoadingDialog(ApplyRefundActivity.this, "正在提交订单，请稍候...", new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("oid", String.valueOf(order.getId())));
        params.add(new BasicNameValuePair("fee", PriceUtils.formatPriceString(order.getPayedFee())));
        params.add(new BasicNameValuePair("message", couses[couseIndex]));

        HttpService.post(Constants.domainHttps() + "/subject/order/refund", params, BaseModel.class, submitHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath ip = adapter.getIndexForPosition(position);
        if (ip.section == 2) {
            couseIndex = ip.row;
            adapter.notifyDataSetChanged();
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            return 4;
        }

        @Override
        public int getCountInSection(int section) {
            if (section <= 1) {
                return 1;
            } else if (section == 2) {
                return couses.length;
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            if (section == 0) {
                SimpleListItem cell = SimpleListItem.create(ApplyRefundActivity.this);
                cell.setTitle("退款金额");
                cell.setSubTitle("¥" + PriceUtils.formatPriceString(order.getPayedFee()));
                cell.getSubTitleTv().setTextColor(getResources().getColor(R.color.text_red));
                return cell;
            } else if (section == 1) {
                SimpleListItem cell = SimpleListItem.create(ApplyRefundActivity.this);
                cell.setTitle("原路退回（3-10个工作日内到账，0手续费）");
                return cell;
            } else {
                ApplyRefundCouseItem cell = ApplyRefundCouseItem.create(ApplyRefundActivity.this);
                cell.setTitle(couses[row]);
                cell.setChecked(row == couseIndex);
                return cell;
            }
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section <= 2) {
                SectionView view = SectionView.create(ApplyRefundActivity.this);
                if (section == 0) {
                    view.setTitle(order.getTitle());
                } else if (section == 1) {
                    view.setTitle("退款方式");
                } else if (section == 2) {
                    view.setTitle("退款原因");
                }
                return view;

            } else if (section == getSectionCount() - 1) {
                LinearLayout ll = new LinearLayout(ApplyRefundActivity.this);
                int padding = UnitTools.dip2px(ApplyRefundActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(ApplyRefundActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("确认退款");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestSubmit();
                    }
                });
                padding = UnitTools.dip2px(ApplyRefundActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_green);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }

}
