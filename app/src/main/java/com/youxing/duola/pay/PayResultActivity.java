package com.youxing.duola.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.RootTabActivity;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.PayCheckModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/21.
 */
public class PayResultActivity extends SGActivity implements View.OnClickListener {

    private String oid;
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

    @Override
    protected void onDestroy() {
        HttpService.abort(freePayHandler);
        HttpService.abort(checkHandler);
        super.onDestroy();
    }

    private void freePay() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("oid", oid));
        params.add(new BasicNameValuePair("coupon", coupon));

        HttpService.post(Constants.domainHttps() + "/payment/prepay/free", params, PayCheckModel.class, freePayHandler);
    }

    private RequestHandler freePayHandler = new RequestHandler() {
        @Override
        public void onRequestFinish(Object response) {
            dismissDialog();

            model = (PayCheckModel) response;
            if (model.getData().isPayed()) {
                paySuccess = true;
            } else {
                setPayFailed();
            }
        }

        @Override
        public void onRequestFailed(BaseModel error) {
            dismissDialog();
            setPayFailed();
        }
    };

    private void checkPayResult() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("oid", oid));

        HttpService.post(Constants.domainHttps() + "/payment/check", params, PayCheckModel.class, checkHandler);
    }

    private RequestHandler checkHandler = new RequestHandler() {
        @Override
        public void onRequestFinish(Object response) {
            dismissDialog();

            model = (PayCheckModel) response;
            if (model.getData().isPayed()) {
                paySuccess = true;
            } else {
                setPayFailed();
            }
        }

        @Override
        public void onRequestFailed(BaseModel error) {
            dismissDialog();
            setPayFailed();
        }
    };

    private void setPayFailed() {
        titleTv.setText("购买失败");
        descTv.setText("请重新确认订单，如有问题可联系客服微信：dorakids01");
        leftBtn.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);
        paySuccess = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(PayResultActivity.this, RootTabActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent home = new Intent(PayResultActivity.this, RootTabActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay_result_left_btn) {
            if (paySuccess) {
                startActivity("duola://bookingsubjectlist");
            }

        } else if (v.getId() == R.id.pay_result_right_btn) {
            startActivity("duola://myorderlist?status=3");
        }
    }
}
