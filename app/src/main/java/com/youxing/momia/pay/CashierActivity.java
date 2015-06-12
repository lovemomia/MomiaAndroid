package com.youxing.momia.pay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.momia.R;
import com.youxing.momia.app.TQActivity;
import com.youxing.momia.pay.views.CashierPayItemView;
import com.youxing.momia.views.SectionView;

/**
 * 收银台
 *
 * Created by Jun Deng on 15/6/9.
 */
public class CashierActivity extends TQActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private CashierPayItemView alipayItem;
    private CashierPayItemView wechatItem;

    private Button doneBtn;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        ListView listView = (ListView)findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        doneBtn = (Button)findViewById(R.id.done);
        doneBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (alipayItem.isChecked()) {
            // 支付宝支付

        } else if (wechatItem.isChecked()) {
            // 微信支付
            final IWXAPI api = WXAPIFactory.createWXAPI(this, null);
            // 将该app注册到微信
            api.registerApp(Constants.WECHAT_APP_ID);

            PayReq request = new PayReq();
            request.appId = Constants.WECHAT_APP_ID;
            request.partnerId = "1900000109";
            request.prepayId= "1101000000140415649af9fc314aa427";
            request.packageValue = "Sign=WXPay";
            request.nonceStr= "1101000000140429eb40476f8896f4c9";
            request.timeStamp= "1398746574";
            request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
            api.sendReq(request);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.section == 1) {
            if (indexPath.row == 0) {
                alipayItem.setChecked(true);
                wechatItem.setChecked(false);

            } else {
                alipayItem.setChecked(false);
                wechatItem.setChecked(true);
            }
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(CashierActivity.this);
        }

        @Override
        public int getSectionCount() {
            return 2;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 3;
            }
            return 2;
        }

        @Override
        public boolean isEnabled(int position) {
            IndexPath indexPath = getIndexForPosition(position);
            if (indexPath.section == 0) {
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0) {
                view = LayoutInflater.from(CashierActivity.this).inflate(R.layout.layout_cashier_item_price, null);
            } else if (section == 1) {
                CashierPayItemView payItemView = (CashierPayItemView)LayoutInflater.from(CashierActivity.this).inflate(R.layout.layout_cashier_item_pay, null);
                if (row == 0) {
                    payItemView.setTitle("支付宝支付");
                    payItemView.setSubTitle("支付宝账户支付，银行卡支付");
                    payItemView.setChecked(true);
                    alipayItem = payItemView;

                } else if (row == 1) {
                    payItemView.setTitle("微信支付");
                    payItemView.setSubTitle("微信钱包支付，银行卡支付");
                    payItemView.setChecked(false);
                    wechatItem = payItemView;
                }
                view = payItemView;
            }
            return view;
        }

        @Override
        public int getHeightForSectionView(int section) {
            if (section == 0) {
                return 0;
            }
            return -1;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section == 1) {
                SectionView sectionView = SectionView.create(CashierActivity.this);
                sectionView.setTitle("支付方式");
                return sectionView;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }
}
