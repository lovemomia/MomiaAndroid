package com.youxing.duola.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.PostOrderModel;
import com.youxing.duola.model.Sku;
import com.youxing.duola.model.SkuListModel;
import com.youxing.duola.order.views.OrderSkuItem;
import com.youxing.duola.order.views.PackageHeaderItem;
import com.youxing.duola.utils.PriceUtils;
import com.youxing.duola.views.SectionView;
import com.youxing.duola.views.SimpleListItem;
import com.youxing.duola.views.StepperView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 提交订单页
 *
 * Created by Jun Deng on 15/6/8.
 * Refactor on 16/3/9 v1.4
 */
public class FillOrderActivity extends SGActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_CONTACT = 1;

    private String id;
    private String coid;
    private String coName;

    private ListView listView;
    private Adapter adapter;
    private TextView priceTv;
    private Button okBtn;

    private SkuListModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_submit);

        id = getIntent().getData().getQueryParameter("id");
        coid = getIntent().getData().getQueryParameter("coid");
        coName = getIntent().getData().getQueryParameter("coname");

        listView = (ListView)findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        priceTv = (TextView) findViewById(R.id.price);
        okBtn = (Button) findViewById(R.id.done);
        okBtn.setOnClickListener(this);

        requestOrder();
    }

    private void requestOrder() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
        if (!TextUtils.isEmpty(coid)) {
            params.add(new BasicNameValuePair("coid", coid));
        }

        HttpService.get(Constants.domain() + "/v2/subject/sku", params, CacheType.DISABLE, SkuListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                model = (SkuListModel) response;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(FillOrderActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    private void requestSubmitOrder() {
        if (!check()) {
            showDialog(FillOrderActivity.this, "您还没有选择课程包");
            return;
        }

        showLoadingDialog(FillOrderActivity.this, "正在提交订单，请稍候...", new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("order", JSON.toJSONString(model.getData())));

        HttpService.post(Constants.domainHttps() + "/subject/order", params, PostOrderModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                PostOrderModel model = (PostOrderModel) response;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://cashpay?order=" + JSON.toJSONString(model.getData()))));
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(FillOrderActivity.this, error.getErrmsg());
            }
        });
    }

    private boolean check() {
        for (Sku sku : model.getData().getSkus()) {
            if (sku.getCount() > 0) {
                return true;
            }
        }

        return false;
    }

    private void updatePrices() {
        double totalPrice = 0;

        for (Sku sku : model.getData().getSkus()) {
            totalPrice += sku.getCount() * sku.getPrice();
        }
        priceTv.setText(PriceUtils.formatPriceString(totalPrice));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.done) {
            requestSubmitOrder();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath ip = adapter.getIndexForPosition(position);
        if (ip.section == 0) {
            // do nothing

        } else if (ip.section == 1 && model.getData().getPackages() != null &&
                model.getData().getPackages().size() > 0) {
            if (ip.row == 0) {
                startActivity("duola://subjectdetail?id=" + this.id);
            }

        } else  {
            startActivityForResult("duola://contact?name="
                    + model.getData().getContact().getName()
                    + "&phone=" + model.getData().getContact().getMobile(), REQUEST_CODE_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CONTACT) {
                String name = data.getStringExtra("name");
                if (!TextUtils.isEmpty(name)) {
                    model.getData().getContact().setName(name);
                }
                String phone = data.getStringExtra("phone");
                if (!TextUtils.isEmpty(phone)) {
                    model.getData().getContact().setMobile(phone);
                }
            }
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(FillOrderActivity.this);
        }

        @Override
        public int getCountInSection(int section) {
            if (model != null) {
                if (section == 0) {
                    return model.getData().getSkus().size();

                } else if (section == 1 && model.getData().getPackages() != null &&
                        model.getData().getPackages().size() > 0) {
                    return 1 + model.getData().getPackages().size();

                } else {
                    return 1;
                }
            }
            return 0;
        }

        @Override
        public int getSectionCount() {
            if (model != null) {
                if (model.getData().getPackages() != null && model.getData().getPackages().size() > 0) {
                    return 3;
                }
                return 2;
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, final int row) {
            View view = null;
            if (section == 0) {
                OrderSkuItem skuItem = OrderSkuItem.create(FillOrderActivity.this);
                final Sku sku = model.getData().getSkus().get(row);
                skuItem.setData(sku, coid == null);
                skuItem.getStepper().setListener(new StepperView.OnNumberChangedListener() {
                    @Override
                    public void onNumberChanged(StepperView stepper) {
                        sku.setCount(stepper.getNumber());
                        updatePrices();
                    }
                });
                if (!TextUtils.isEmpty(coid)) {
                    skuItem.getStepper().setNumber(1);
                    sku.setCount(1);
                    updatePrices();
                }
                view = skuItem;

            } else if (section == 1 && model.getData().getPackages() != null &&
                    model.getData().getPackages().size() > 0) {
                if (row == 0) {
                    PackageHeaderItem item = PackageHeaderItem.create(FillOrderActivity.this);
                    item.getPackageTv().setText(coName);
                    view = item;

                } else {
                    OrderSkuItem skuItem = OrderSkuItem.create(FillOrderActivity.this);
                    Sku sku = model.getData().getPackages().get(row - 1);
                    skuItem.setData(sku, true);
                    skuItem.getStepper().setVisibility(View.GONE);
                    view = skuItem;
                }

            } else {
                SimpleListItem simpleListItem = SimpleListItem.create(FillOrderActivity.this);
                simpleListItem.setShowArrow(true);
                simpleListItem.setTitle("联系人信息");
                simpleListItem.setSubTitle(model.getData().getContact().getMobile());
                view = simpleListItem;
            }
            return view;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section == 0) {
                SectionView view = SectionView.create(FillOrderActivity.this);
                if (coid != null) {
                    view.setTitle("购买单次课程");
                } else {
                    view.setTitle("选择课程包");
                }
                return view;
            }
            return super.getViewForSection(convertView, parent, section);
        }

    }

}
