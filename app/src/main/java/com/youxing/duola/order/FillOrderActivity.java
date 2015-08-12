package com.youxing.duola.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.model.AccountModel;
import com.youxing.duola.model.FillOrderModel;
import com.youxing.duola.model.Sku;
import com.youxing.duola.order.views.OrderNumberItem;
import com.youxing.duola.order.views.OrderSkuItem;
import com.youxing.duola.views.SectionView;
import com.youxing.duola.views.SimpleListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 提交订单页
 *
 * Created by Jun Deng on 15/6/8.
 */
public class FillOrderActivity extends DLActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_SELECT_PERSON = 1;

    private ListView listView;
    private Adapter adapter;

    private boolean isShowAllSku;
    private boolean needRealName;
    private int selectSkuIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_submit);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        findViewById(R.id.done).setOnClickListener(this);

        requestOrder();
    }

    private void requestOrder() {
        showLoading();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", getIntent().getData().getQueryParameter("id")));

        HttpService.get(Constants.domain() + "/product/order", params, CacheType.DISABLE, FillOrderModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(BaseModel response) {
                dismissLoading();

                FillOrderModel model = (FillOrderModel) response;
                adapter.setData(model);
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissLoading();
                showDialog(FillOrderActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
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

        FillOrderModel model;

        public void setData(FillOrderModel model) {
            this.model = model;
            notifyDataSetChanged();
        }

        public Adapter() {
            super(FillOrderActivity.this);
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                if (model.getData().getSkus().size() > 3 && !isShowAllSku) {
                    return 4;
                } else {
                    return model.getData().getSkus().size();
                }

            } else if (section == 1) {
                Sku sku = model.getData().getSkus().get(selectSkuIndex);
                if (sku.getStock() > 0) {
                    return sku.getPrices().size();
                } else {
                    return 0;
                }

            } else {
                Sku sku = model.getData().getSkus().get(selectSkuIndex);
                if (sku.isNeedRealName()) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }

        @Override
        public int getSectionCount() {
            if (model == null) {
                return 0;
            }
            return 3;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0) {
                view = OrderSkuItem.create(FillOrderActivity.this);

            } else if (section == 1) {
                view = OrderNumberItem.create(FillOrderActivity.this);

            } else if (section == 2) {
                view = SimpleListItem.create(FillOrderActivity.this);
            }
            return view;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            SectionView view = SectionView.create(FillOrderActivity.this);
            if (section == 0) {
                view.setTitle("选择场次");
            } else if (section == 1) {
                view.setTitle("选择出行人数");
            } else if (section == 2) {
                return super.getViewForSection(convertView, parent, section);
            }
            return view;
        }
    }
}
