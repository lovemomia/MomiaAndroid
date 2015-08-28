package com.youxing.duola.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.model.FillOrderModel;
import com.youxing.duola.model.OrderDetailModel;
import com.youxing.duola.model.Sku;
import com.youxing.duola.order.views.OrderNumberItem;
import com.youxing.duola.order.views.OrderSkuItem;
import com.youxing.duola.utils.PriceUtils;
import com.youxing.duola.views.SectionView;
import com.youxing.duola.views.SimpleListItem;
import com.youxing.duola.views.StepperGroup;
import com.youxing.duola.views.StepperView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 提交订单页
 *
 * Created by Jun Deng on 15/6/8.
 */
public class FillOrderActivity extends DLActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_SELECT_PERSON = 1;
    private static final int REQUEST_CODE_CONTACT = 2;

    private ListView listView;
    private Adapter adapter;
    private TextView priceTv;
    private Button okBtn;

    private FillOrderModel model;
    private boolean isShowAllSku;
    private boolean needRealName;
    private int selectSkuIndex = -1;
    private int selectAdultNum;
    private int selectChildNum;
    private boolean isSelectPerson;
    private SubmitOrder submitOrder;

    private StepperGroup stepperGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_submit);
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

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", getIntent().getData().getQueryParameter("id")));

        HttpService.get(Constants.domain() + "/product/order", params, CacheType.DISABLE, FillOrderModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(BaseModel response) {
                dismissDialog();

                FillOrderModel model = (FillOrderModel) response;
                FillOrderActivity.this.model = model;
                adapter.notifyDataSetChanged();

                submitOrder = new SubmitOrder();
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
            return;
        }

        showLoadingDialog(FillOrderActivity.this, "正在提交订单，请稍候...", new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("order", JSON.toJSONString(submitOrder)));

        HttpService.post(Constants.domainHttps() + "/order", params, OrderDetailModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(BaseModel response) {
                dismissDialog();

                OrderDetailModel model = (OrderDetailModel) response;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://cashpay?pom=" + JSON.toJSONString(model))));
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(FillOrderActivity.this, error.getErrmsg());
            }
        });
    }

    private boolean check() {
        if (submitOrder == null) {
            return false;
        }
        if (selectSkuIndex == -1) {
            return false;
        }
        Sku sku = model.getData().getSkus().get(selectSkuIndex);
        submitOrder.productId = sku.getProductId();
        submitOrder.skuId = sku.getSkuId();
        submitOrder.mobile = model.getData().getContacts().getMobile();
        updatePrices();

        return true;
    }

    private void updatePrices() {
        submitOrder.prices.clear();

        int i = 0;
        for (StepperView sv : stepperGroup.getStepperList()) {
            int number = sv.getNumber();
            if (number > 0) {
                Sku.Price sp = model.getData().getSkus().get(selectSkuIndex).getPrices().get(i);
                SubmitPrice subPrice = new SubmitPrice();
                subPrice.adult = sp.getAdult();
                subPrice.child = sp.getChild();
                subPrice.price = sp.getPrice();
                subPrice.count = number;
                submitOrder.prices.add(subPrice);
            }
            i ++;
        }
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
            if (ip.row < model.getData().getSkus().size()) {
                if (selectSkuIndex == ip.row) {
                    return;
                }
                if (isSkuSelectAble(model.getData().getSkus().get(ip.row))) {
                    selectSkuIndex = ip.row;

                    //reset
                    stepperGroup = null;
                    selectAdultNum = 0;
                    selectChildNum = 0;
                    isSelectPerson = false;

                    adapter.notifyDataSetChanged();
                }

            } else {
                isShowAllSku = true;
                adapter.notifyDataSetChanged();
            }
        } else if (ip.section == 2) {
            Sku sku = model.getData().getSkus().get(selectSkuIndex);
            if (sku.isNeedRealName() && ip.row == 0) {
                if (selectAdultNum > 0 || selectChildNum > 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("duola://orderperson"));
                    intent.putExtra("select", true);
                    intent.putExtra("adult", selectAdultNum);
                    intent.putExtra("child", selectChildNum);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_PERSON);
                }

            } else {
                startActivityForResult("duola://contact?name="
                        + model.getData().getContacts().getName()
                        + "&phone=" + model.getData().getContacts().getMobile(), REQUEST_CODE_CONTACT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PERSON) {
                String selectPersons = data.getStringExtra("selectPersons");
                if (TextUtils.isEmpty(selectPersons)) {
                    return;
                }
                List<Long> idList = (List<Long>)JSON.parse(selectPersons);
                submitOrder.setParticipants(idList);
                isSelectPerson = true;
                adapter.notifyDataSetChanged();

            } else if (requestCode == REQUEST_CODE_CONTACT) {
                String name = data.getStringExtra("name");
                if (!TextUtils.isEmpty(name)) {
                    submitOrder.setContacts(name);
                    model.getData().getContacts().setName(name);
                }
                String phone = data.getStringExtra("phone");
                if (!TextUtils.isEmpty(phone)) {
                    submitOrder.setMobile(phone);
                    model.getData().getContacts().setMobile(phone);
                }
            }
        }
    }

    public boolean isSkuSelectAble(Sku sku) {
        if (sku.getType() != 1 && sku.getStock() == 0) {
            return false;
        }
        return true;
    }

    class Adapter extends GroupStyleAdapter {

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
                if (selectSkuIndex == -1) {
                    return 0;
                }
                Sku sku = model.getData().getSkus().get(selectSkuIndex);
                if (sku.getStock() > 0) {
                    return sku.getPrices().size();
                } else {
                    return 0;
                }

            } else {
                if (selectSkuIndex == -1) {
                    return 0;
                }
                Sku sku = model.getData().getSkus().get(selectSkuIndex);
                if (sku.isNeedRealName()) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }

        @Override
        public void notifyDataSetChanged() {
            if (model != null && selectSkuIndex == -1) {
                for (int i = 0; i < model.getData().getSkus().size(); i++) {
                    Sku sku = model.getData().getSkus().get(i);
                    if (isSkuSelectAble(sku)) {
                        selectSkuIndex = i;
                        break;
                    }
                }
            }
            super.notifyDataSetChanged();
        }

        @Override
        public int getSectionCount() {
            if (model == null) {
                return 0;
            }
            return 3;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, final int row) {
            View view = null;
            if (section == 0) {
                if (row < model.getData().getSkus().size()) {
                    OrderSkuItem skuItem = OrderSkuItem.create(FillOrderActivity.this);
                    skuItem.setData(model.getData().getSkus().get(row));
                    view = skuItem;

                    if (row == selectSkuIndex) {
                        skuItem.setSelect(true);
                    } else {
                        skuItem.setSelect(false);
                    }

                } else {
                    view = LayoutInflater.from(FillOrderActivity.this).inflate(R.layout.layout_order_other_sku, null);
                }

            } else if (section == 1) {
                OrderNumberItem numberItem = OrderNumberItem.create(FillOrderActivity.this);
                Sku.Price price = model.getData().getSkus().get(selectSkuIndex).getPrices().get(row);
                numberItem.setData(price);

                if (stepperGroup == null) {
                    stepperGroup = new StepperGroup();
                    int stock = model.getData().getSkus().get(selectSkuIndex).getStock();
                    stepperGroup.setMax(stock == 0 ? Integer.MAX_VALUE : stock);
                    stepperGroup.setMin(0);
                    stepperGroup.addStepper(numberItem.getStepperView());
                    stepperGroup.setListener(new StepperView.OnNumberChangedListener() {
                        @Override
                        public void onNumberChanged(StepperView stepper) {
                            double totalPrice = 0;
                            int adultNum = 0;
                            int childNum = 0;
                            int i = 0;
                            for (StepperView sv : stepperGroup.getStepperList()) {
                                int number = sv.getNumber();
                                Sku.Price skuPrice = model.getData().getSkus().get(selectSkuIndex).getPrices().get(row);
                                double price = skuPrice.getPrice();
                                totalPrice += price * number;
                                adultNum += skuPrice.getAdult() * number;
                                childNum += skuPrice.getChild() * number;

                                i ++;
                            }
                            priceTv.setText(PriceUtils.formatPriceString(totalPrice));
                            FillOrderActivity.this.selectAdultNum = adultNum;
                            FillOrderActivity.this.selectChildNum = childNum;
                        }
                    });
                } else {
                    if (row < stepperGroup.getStepperList().size()) {
                        numberItem.getStepperView().setNumber(stepperGroup.getStepperList().get(row).getNumber());
                    }
                }

                view = numberItem;

            } else if (section == 2) {
                SimpleListItem simpleListItem = SimpleListItem.create(FillOrderActivity.this);
                simpleListItem.setShowArrow(true);
                Sku sku = model.getData().getSkus().get(selectSkuIndex);
                if (sku.isNeedRealName() && row == 0) {
                    simpleListItem.setTitle("出行人");
                    if (isSelectPerson) {
                        StringBuilder sb = new StringBuilder();
                        if (selectAdultNum > 0) {
                            sb.append(selectAdultNum + "成人");
                        }
                        if (selectChildNum > 0) {
                            sb.append(selectChildNum + "小孩");
                        }
                        simpleListItem.setSubTitle(sb.toString());
                    }

                } else {
                    simpleListItem.setTitle("联系人信息");
                    simpleListItem.setSubTitle(model.getData().getContacts().getMobile());
                }
                view = simpleListItem;
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

    class SubmitOrder {
        long productId;
        long skuId;
        String contacts;
        String mobile;
        List<Long> participants;
        List<SubmitPrice> prices = new ArrayList<>();

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public long getSkuId() {
            return skuId;
        }

        public void setSkuId(long skuId) {
            this.skuId = skuId;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public List<Long> getParticipants() {
            return participants;
        }

        public void setParticipants(List<Long> participants) {
            this.participants = participants;
        }

        public List<SubmitPrice> getPrices() {
            return prices;
        }

        public void setPrices(List<SubmitPrice> prices) {
            this.prices = prices;
        }
    }

    class SubmitPrice {
        int adult;
        int child;
        double price;
        int count;

        public int getAdult() {
            return adult;
        }

        public void setAdult(int adult) {
            this.adult = adult;
        }

        public int getChild() {
            return child;
        }

        public void setChild(int child) {
            this.child = child;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
