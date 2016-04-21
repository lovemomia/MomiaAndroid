package com.youxing.duola.home;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.CouponShareModel;
import com.youxing.duola.views.ShareDialog;

/**
 * 邀请好友
 *
 * Created by Jun Deng on 16/3/14.
 */
public class ShareActivity extends SGActivity implements OnClickListener {

    private TextView contentTv;

    private CouponShareModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        contentTv = (TextView) findViewById(R.id.content);
        findViewById(R.id.share).setOnClickListener(this);

        requestData();
    }

    @Override
    protected void onDestroy() {
        HttpService.abort(handler);
        super.onDestroy();
    }

    private void requestData() {
        showLoadingDialog(this);

        HttpService.get(Constants.domain() + "/coupon/share", null, CacheType.DISABLE, CouponShareModel.class, handler);
    }

    private RequestHandler handler = new RequestHandler() {
        @Override
        public void onRequestFinish(Object response) {
            dismissDialog();
            model = (CouponShareModel) response;
            contentTv.setText(model.getData().getDesc());
        }

        @Override
        public void onRequestFailed(BaseModel error) {
            showDialog(ShareActivity.this, error.getErrmsg());
        }
    };

    @Override
    public void onClick(View v) {
        if (model != null) {
            new ShareDialog(this, model.getData().getUrl(), model.getData().getTitle(),
                    model.getData().getAbstracts(), model.getData().getCover()).show();
        }
    }
}
