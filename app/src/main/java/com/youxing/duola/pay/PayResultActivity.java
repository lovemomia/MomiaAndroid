package com.youxing.duola.pay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.RootTabActivity;
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.model.PayCheckModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/21.
 */
public class PayResultActivity extends DLActivity implements View.OnClickListener {

    private String oid;
    private String pid;
    private String sid;
    private String coupon;
    private String free;

    private TextView titleTv;
    private TextView descTv;
    private Button leftBtn;
    private Button rightBtn;

    private PayCheckModel model;
    private boolean paySuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);

        oid = getIntent().getData().getQueryParameter("oid");
        pid = getIntent().getData().getQueryParameter("pid");
        sid = getIntent().getData().getQueryParameter("sid");
        coupon = getIntent().getData().getQueryParameter("coupon");
        free = getIntent().getData().getQueryParameter("free");

        titleTv = (TextView) findViewById(R.id.pay_result_title);
        descTv = (TextView) findViewById(R.id.pay_result_desc);
        leftBtn = (Button) findViewById(R.id.pay_result_left_btn);
        leftBtn.setOnClickListener(this);
        rightBtn = (Button) findViewById(R.id.pay_result_right_btn);
        rightBtn.setOnClickListener(this);

        if ("1".equals(free)) {
            freePay();
        } else {
            checkPayResult();
        }
    }

    private void freePay() {
        showLoading();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("oid", oid));
        params.add(new BasicNameValuePair("pid", pid));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("coupon", coupon));

        HttpService.post(Constants.domainHttps() + "/payment/prepay/free", params, PayCheckModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(BaseModel response) {
                dismissLoading();

                model = (PayCheckModel) response;
                paySuccess = true;
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissLoading();
                titleTv.setText("购买失败");
                descTv.setText("请重新确认订单，如有问题可拨打客服热线：021-62578700");
                leftBtn.setText("联系客服");
                paySuccess = false;
            }
        });
    }

    private void checkPayResult() {
        showLoading();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("oid", oid));
        params.add(new BasicNameValuePair("pid", pid));
        params.add(new BasicNameValuePair("sid", sid));

        HttpService.post(Constants.domainHttps() + "/payment/check", params, PayCheckModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(BaseModel response) {
                dismissLoading();

                model = (PayCheckModel) response;
                paySuccess = true;
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissLoading();
                titleTv.setText("购买失败");
                descTv.setText("请重新确认订单，如有问题可拨打客服热线：021-62578700");
                leftBtn.setText("联系客服");
                paySuccess = false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(PayResultActivity.this, RootTabActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }

    @Override
    public void onTitleBackClicked() {
        Intent home = new Intent(PayResultActivity.this, RootTabActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay_result_left_btn) {
            if (paySuccess) {
                // TODO 分享


            } else {
                startActivity("tel://02162578700");
            }

        } else if (v.getId() == R.id.pay_result_right_btn) {
            startActivity("duola://myorderlist?status=3");
        }
    }
}
