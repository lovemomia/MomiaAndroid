package com.youxing.duola.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.AlipayOrderModel;
import com.youxing.duola.model.CouponPriceModel;
import com.youxing.duola.model.Order;
import com.youxing.duola.model.WechatPayModel;
import com.youxing.duola.pay.views.CashierPayItemView;
import com.youxing.duola.utils.PriceUtils;
import com.youxing.duola.views.SectionView;
import com.youxing.duola.views.SimpleListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 收银台
 *
 * Created by Jun Deng on 15/6/9.
 */
public class CashierActivity extends SGActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_COUPON = 1;

    private Order order;

    private CouponPriceModel couponPrice;
    private long couponId;

    private CashierPayItemView alipayItem;
    private CashierPayItemView wechatItem;

    private Adapter adapter;

    private static final String BROADCAST_PAY_RESULT = "com.youxing.duola.broadcast.PAY_RESULT";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_PAY_RESULT)) {
                int errCode = intent.getIntExtra("errCode", 0);
                if (errCode == 0) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://payresult?oid=" + order.getId())));

                } else {
                    showDialog(CashierActivity.this, "支付失败");
                }
            }
        }
    };

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://payresult?oid=" + order.getId())));
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(CashierActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(CashierActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(CashierActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        String orderStr = getIntent().getData().getQueryParameter("order");
        order = JSON.parseObject(orderStr, Order.class);

        ListView listView = (ListView)findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_PAY_RESULT);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_COUPON && resultCode == RESULT_OK) {
            couponId = data.getLongExtra("coupon", 0);
            requestRefreshPrice(couponId);
        }
    }

    private void requestRefreshPrice(long couponId) {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("oid", String.valueOf(order.getId())));
        params.add(new BasicNameValuePair("coupon", String.valueOf(couponId)));
        HttpService.get(Constants.domain() + "/subject/order/coupon", params, CacheType.DISABLE,
                CouponPriceModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                couponPrice = (CouponPriceModel)response;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(CashierActivity.this, error.getErrmsg());
                CashierActivity.this.couponId = 0;
            }
        });
    }

    @Override
    public void onClick(View v) {
        // 0元购
        if (order.getTotalFee() == 0 || (couponPrice != null && couponPrice.getData() == 0)) {
            startActivity("duola://payresult?oid=" + order.getId()
                    + "&coupon=" + couponId + "&free=1");
            return;
        }

        if (alipayItem.isChecked()) {
            // 支付宝支付
            startAlipay();

        } else if (wechatItem.isChecked()) {
            // 微信支付
            startWXPay();
        }
    }

    public void startWXPay() {
        showLoadingDialog(CashierActivity.this, "正在准备支付，请稍候...", null);

        // 微信支付
        final IWXAPI api = WXAPIFactory.createWXAPI(CashierActivity.this, Constants.WECHAT_APP_ID);
        if (!api.isWXAppInstalled() || !api.isWXAppSupportAPI()) {
            dismissDialog();
            showDialog(CashierActivity.this, "使用微信支付功能需要您安装最新的微信客户端");
            return;
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("type", "APP"));
        params.add(new BasicNameValuePair("oid", String.valueOf(order.getId())));
        if (couponId > 0) {
            params.add(new BasicNameValuePair("coupon", String.valueOf(couponId)));
        }

        HttpService.post(Constants.domainHttps() + "/payment/prepay/weixin", params, WechatPayModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                WechatPayModel model = (WechatPayModel) response;

                // 将该app注册到微信
                api.registerApp(model.getData().getAppid());

                PayReq request = new PayReq();
                request.appId = model.getData().getAppid();
                request.partnerId = model.getData().getPartnerid();
                request.prepayId= model.getData().getPrepayid();
                request.packageValue = model.getData().getPackage_app();
                request.nonceStr= model.getData().getNoncestr();
                request.timeStamp= model.getData().getTimestamp();
                request.sign= model.getData().getSign();

                api.sendReq(request);
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(CashierActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    public void startAlipay() {
        showLoadingDialog(CashierActivity.this, "正在准备支付，请稍候...", null);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("type", "APP"));
        params.add(new BasicNameValuePair("oid", String.valueOf(order.getId())));
        if (couponId > 0) {
            params.add(new BasicNameValuePair("coupon", String.valueOf(couponId)));
        }

        HttpService.post(Constants.domainHttps() + "/payment/prepay/alipay", params, AlipayOrderModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AlipayOrderModel model = (AlipayOrderModel) response;
                final String orderInfo = model.getData().getOrderInfo();

                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(CashierActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(orderInfo);

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(CashierActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.section == 1) {
            startActivityForResult("duola://couponlist?oid=" +
                    order.getId() + "&select=1", REQUEST_CODE_COUPON);

        } else if (indexPath.section == 2) {
            if (indexPath.row == 0) {
                alipayItem.setChecked(false);
                wechatItem.setChecked(true);

            } else {
                alipayItem.setChecked(true);
                wechatItem.setChecked(false);
            }
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(CashierActivity.this);
        }

        @Override
        public int getSectionCount() {
            return 4;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 3) {
                return 0;
            }
            return 2;
        }

        @Override
        public boolean isEnabled(int position) {
            IndexPath indexPath = getIndexForPosition(position);
            if (indexPath.section == 0) {
                return false;
            } else if (indexPath.section == 1 && indexPath.row == 1) {
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0) {
                SimpleListItem listItem = SimpleListItem.create(CashierActivity.this);
                if (row == 0) {
                    listItem.setTitle("订单数量");
                    listItem.setSubTitle(String.valueOf(order.getCount()));
                } else {
                    listItem.setTitle("总价");
                    listItem.setSubTitle("￥" + PriceUtils.formatPriceString(order.getTotalFee()));
                }
                view = listItem;
            } else if (section == 1) {
                SimpleListItem listItem = SimpleListItem.create(CashierActivity.this);
                if (row == 0) {
                    listItem.setTitle("红包");
                    listItem.getSubTitleTv().setTextColor(getResources().getColor(R.color.app_theme));
                    listItem.setSubTitle("使用红包");
                    listItem.setShowArrow(true);
                } else {
                    listItem.setTitle("还需支付");
                    if (couponPrice != null) {
                        listItem.setSubTitle("￥" + PriceUtils.formatPriceString(couponPrice.getData()));
                    } else {
                        listItem.setSubTitle("￥" + PriceUtils.formatPriceString(order.getTotalFee()));
                    }
                }
                view = listItem;

            } else if (section == 2) {
                CashierPayItemView payItemView = CashierPayItemView.create(CashierActivity.this);
                if (row == 0) {
                    payItemView.setIcon(R.drawable.ic_wxpay);
                    payItemView.setTitle("微信支付");
                    payItemView.setSubTitle("推荐已安装微信的用户使用");
                    payItemView.setChecked(false);
                    wechatItem = payItemView;

                } else if (row == 1) {
                    payItemView.setIcon(R.drawable.ic_alipay);
                    payItemView.setTitle("支付宝支付");
                    payItemView.setSubTitle("推荐支付宝用户使用");
                    payItemView.setChecked(true);
                    alipayItem = payItemView;

                }
                view = payItemView;
            } else if (section == 3) {
            }
            return view;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section == 2) {
                SectionView sectionView = SectionView.create(CashierActivity.this);
                sectionView.setTitle("选择支付方式");
                return sectionView;
            } else if (section == 3) {
                LinearLayout ll = new LinearLayout(CashierActivity.this);
                int padding = UnitTools.dip2px(CashierActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(CashierActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("确认支付");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(CashierActivity.this);
                padding = UnitTools.dip2px(CashierActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_red);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }
}
