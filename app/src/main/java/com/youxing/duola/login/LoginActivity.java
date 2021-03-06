package com.youxing.duola.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.AccountModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/10.
 */
public class LoginActivity extends SGActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_REGISTER = 1;
    public static final int REQUEST_CODE_FORGETPWD = 2;
    private static final int MENU_REGISTER = 1;

    private EditText phoneEdit;
    private EditText pwdEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        phoneEdit = (EditText) findViewById(R.id.login_phone_edit);
        pwdEdit = (EditText) findViewById(R.id.login_pwd_edit);
        findViewById(R.id.login_forget_pwd).setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_REGISTER, 0, "注册").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AccountService.instance().cancelLogin();

        } else if (item.getItemId() == MENU_REGISTER) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("duola://register"));
            startActivityForResult(intent, REQUEST_CODE_REGISTER);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_forget_pwd) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("duola://forgetpwd"));
            startActivityForResult(intent, REQUEST_CODE_FORGETPWD);

        } else if (v.getId() == R.id.login_btn) {
            if (check()) {
                requestLogin();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String destination = getIntent().getStringExtra("_destination");
            if (!TextUtils.isEmpty(destination)) {
                startActivity(destination);
            }
            finish();
        }
    }

    private boolean check() {
        if (phoneEdit.getText().toString().trim().length() == 0) {
            showDialog(this, "手机号不能为空");
            return false;
        }
        if (pwdEdit.getText().toString().trim().length() == 0) {
            showDialog(this, "密码不能为空");
            return false;
        }
        return true;
    }

    private void requestLogin() {
        showLoadingDialog(this, "正在登录，请稍候...", null);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("mobile", phoneEdit.getText().toString().trim()));
        params.add(new BasicNameValuePair("password", pwdEdit.getText().toString().trim()));

        HttpService.post(Constants.domain() + "/auth/login", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                AccountService.instance().dispatchAccountChanged(model.getData());

                String destination = getIntent().getStringExtra("_destination");
                if (!TextUtils.isEmpty(destination)) {
                    startActivity(getIntent().getStringExtra("_destination"));
                }

                finish();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(LoginActivity.this, error.getErrmsg());
            }
        });

    }

}
