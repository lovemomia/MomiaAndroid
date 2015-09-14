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
import com.youxing.duola.order.views.OrderPlaceItem;
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
    private List<Sku> showSkus;
    private boolean isShowAllSku;
    private int selectSkuIndex = -1;
    private boolean isSelectPerson;
    private SubmitOrder submitOrder;

    private StepperGroup stepperGroup;

    private List<Sku> selectPlaceSkus;
    private boolean isShowPlaces;
    private FillOrderModel.Place selectedPlace;

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

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", getIntent().getData().getQueryParameter("id")));

        HttpService.get(Constants.domain() + "/product/order", params, CacheType.DISABLE, FillOrderModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                FillOrderModel model = (FillOrderModel) response;
                FillOrderActivity.this.model = model;
                if (model.getData().getPlaces() != null && model.getData().getPlaces().size() > 0) {
                    isShowPlaces = true;
                    selectedPlace = model.getData().getPlaces().get(0);
                    selectPlaceSkus = new ArrayList<Sku>();
                    resetSelectPlaceSkus();
                    showSkus = selectPlaceSkus;
                } else {
                    showSkus = model.getData().getSkus();
                }

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

    private void resetSelectPlaceSkus() {
        if (selectedPlace == null || model == null) {
            return;
        }

        selectPlaceSkus.clear();
        for (Sku sku : model.getData().getSkus()) {
            if (sku.getPlaceId() == selectedPlace.getId()) {
                selectPlaceSkus.add(sku);
            }
        }
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

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("order", JSON.toJSONString(submitOrder)));

        HttpService.post(Constants.domainHttps() + "/order", params, OrderDetailModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
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
        Sku sku = showSkus.get(selectSkuIndex);
        submitOrder.productId = sku.getProductId();
        submitOrder.skuId = sku.getSkuId();
        submitOrder.mobile = model.getData().getContacts().getMobile();
        updatePrices();

        return true;
    }

    private void updatePrices() {
        submitOrder.prices.clear();

        for (int i = 0; i < showSkus.get(selectSkuIndex).getPrices().size(); i++) {
            Sku.Price sp = showSkus.get(selectSkuIndex).getPrices().get(i);
            Integer count = stepperGroup.getCountMap().get(i);
            if (count != null) {
                SubmitPrice subPrice = new SubmitPrice();
                subPrice.adult = sp.getAdult();
                subPrice.child = sp.getChild();
                subPrice.price = sp.getPrice();
                subPrice.count = count;
                submitOrder.prices.add(subPrice);
            }
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
        int sec = ip.section;
        if (isShowPlaces) {
            sec--;
        }
        if (sec == -1) {
            FillOrderModel.Place place = model.getData().getPlaces().get(ip.row);
            if (place.equals(selectedPlace)) {
                return;
            }
            selectedPlace = place;
            // reset
            resetSelectPlaceSkus();
            selectSkuIndex = -1;
            stepperGroup = null;
            isSelectPerson = false;
            isShowAllSku = false;
            priceTv.setText(PriceUtils.formatPriceString(0));
            adapter.notifyDataSetChanged();

        } else if (sec == 0) {
            if (ip.row < showSkus.size()) {
                if (selectSkuIndex == ip.row) {
                    return;
                }
                if (isSkuSelectAble(showSkus.get(ip.row))) {
                    selectSkuIndex = ip.row;

                    //reset
                    stepperGroup = null;
                    isSelectPerson = false;
                    priceTv.setText(PriceUtils.formatPriceString(0));
                    adapter.notifyDataSetChanged();
                }

            } else {
                isShowAllSku = true;
                adapter.notifyDataSetChanged();
            }
        } else if (sec == 2) {
            Sku sku = showSkus.get(selectSkuIndex);
            if (sku.isNeedRealName() && ip.row == 0) {
                if (stepperGroup.getSelectAdultNum() > 0 || stepperGroup.getSelectChildNum() > 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("duola://orderperson"));
                    intent.putExtra("select", true);
                    intent.putExtra("adult", stepperGroup.getSelectAdultNum());
                    intent.putExtra("child", stepperGroup.getSelectChildNum());
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
            int sec = section;
            if (isShowPlaces) {
                sec--;
            }
            if (sec == -1) {
                return model.getData().getPlaces().size();

            } else if (sec == 0) {
                if (showSkus.size() > 3 && !isShowAllSku) {
                    return 4;
                } else {
                    return showSkus.size();
                }

            } else if (sec == 1) {
                if (selectSkuIndex == -1) {
                    return 0;
                }
                Sku sku = showSkus.get(selectSkuIndex);
                if (sku.getStock() > 0) {
                    return sku.getPrices().size();
                } else {
                    return 0;
                }

            } else {
                if (selectSkuIndex == -1) {
                    return 0;
                }
                Sku sku = showSkus.get(selectSkuIndex);
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
                for (int i = 0; i < showSkus.size(); i++) {
                    Sku sku = showSkus.get(i);
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
            if (isShowPlaces) {
                return 4;
            }
            return 3;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, final int row) {
            View view = null;
            int sec = section;
            if (isShowPlaces) {
                sec--;
            }

            if (sec == -1) {
                FillOrderModel.Place place = model.getData().getPlaces().get(row);
                OrderPlaceItem placeItem = OrderPlaceItem.create(FillOrderActivity.this);
                placeItem.setData(place);
                placeItem.setSelect(place.equals(selectedPlace));
                view = placeItem;

            } else if (sec == 0) {
                if (row < showSkus.size()) {
                    OrderSkuItem skuItem = OrderSkuItem.create(FillOrderActivity.this);
                    skuItem.setData(showSkus.get(row));
                    view = skuItem;

                    if (row == selectSkuIndex) {
                        skuItem.setSelect(true);
                    } else {
                        skuItem.setSelect(false);
                    }

                } else {
                    view = LayoutInflater.from(FillOrderActivity.this).inflate(R.layout.layout_order_other_sku, null);
                }

            } else if (sec == 1) {
                OrderNumberItem numberItem = OrderNumberItem.create(FillOrderActivity.this);
                Sku.Price price = showSkus.get(selectSkuIndex).getPrices().get(row);
                numberItem.setData(price);
                numberItem.getStepperView().setTag(row);

                numberItem.getStepperView().setListener(new StepperView.OnNumberChangedListener() {
                    @Override
                    public void onNumberChanged(StepperView stepper) {
                        Integer index = (Integer)stepper.getTag();
                        stepperGroup.getCountMap().put(index, stepper.getNumber());
                        stepperGroup.compute(showSkus.get(selectSkuIndex).getPrices());
                        priceTv.setText(PriceUtils.formatPriceString(stepperGroup.getTotalPrice()));
                    }
                });

                if (stepperGroup == null) {
                    stepperGroup = new StepperGroup();
                    int stock = showSkus.get(selectSkuIndex).getStock();
                    stepperGroup.setMax(stock == 0 ? Integer.MAX_VALUE : stock);
                    stepperGroup.setMin(0);
                } else {
                    Integer count = stepperGroup.getCountMap().get(row);
                    if (count == null) {
                        count = 0;
                    }
                    numberItem.getStepperView().setNumber(count);
                }
                stepperGroup.adjustStepper(numberItem.getStepperView());

                view = numberItem;

            } else if (sec == 2) {
                SimpleListItem simpleListItem = SimpleListItem.create(FillOrderActivity.this);
                simpleListItem.setShowArrow(true);
                Sku sku = showSkus.get(selectSkuIndex);
                if (sku.isNeedRealName() && row == 0) {
                    simpleListItem.setTitle("出行人");
                    if (isSelectPerson) {
                        StringBuilder sb = new StringBuilder();
                        if (stepperGroup.getSelectAdultNum() > 0) {
                            sb.append(stepperGroup.getSelectAdultNum() + "成人");
                        }
                        if (stepperGroup.getSelectChildNum() > 0) {
                            sb.append(stepperGroup.getSelectChildNum() + "小孩");
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
            int sec = section;
            if (isShowPlaces) {
                sec--;
            }
            if (sec == -1) {
                view.setTitle("选择区域");

            } else if (sec == 0) {
                view.setTitle("选择场次");

            } else if (sec == 1) {
                view.setTitle("选择出行人数");

            } else if (sec == 2) {
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
        List<SubmitPrice> prices = new ArrayList<SubmitPrice>();

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
