package com.youxing.duola.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.umeng.analytics.MobclickAgent;
import com.youxing.common.app.YXActivity;
import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;
import com.youxing.duola.RootTabActivity;
import com.youxing.duola.views.ProgressHUD;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class SGActivity extends YXActivity {

    private Dialog managerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(this instanceof RootTabActivity) && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        if (showTitleShadow()) {
            addShadowView();
        }
    }

    protected boolean showTitleShadow() {
        return true;
    }

    private void addShadowView() {
        View shadow = new View(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, UnitTools.dip2px(this, 1));
        shadow.setBackgroundResource(R.drawable.shadow);
        addContentView(shadow, lp);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public View inflateView(int resId) {
        return LayoutInflater.from(this).inflate(resId, null);
    }

    // *************** UI操作 ***************

    public void showLoadingDialog(Context context) {
        showLoadingDialog(context, null, null);
    }

    public void showLoadingDialog(Context context, String msg,
                                     DialogInterface.OnCancelListener cancelListener) {
        dismissDialog();

        managerDialog = ProgressHUD.show(context, msg, true, true, cancelListener);
    }

    public void showDialog(Context context, String message) {
        showDialog(context, null, message, "确定", null, null, null);
    }

    public void showDialog(Context context, String message, DialogInterface.OnClickListener okListener) {
        showDialog(context, null, message, "确定", okListener, null, null);
    }

    public void showDialog(Context context, String title, String message, String ok) {
        showDialog(context, title, message, ok, null, null, null);
    }

    public void showDialog(Context context, String title,
                                    String message, String ok,
                                    DialogInterface.OnClickListener okListener, String cancel,
                                    DialogInterface.OnClickListener cancelListener) {
        dismissDialog();

        AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setTitle(title);
        dlg.setMessage(message);
        if (!TextUtils.isEmpty(ok)) {
            dlg.setButton(DialogInterface.BUTTON_POSITIVE, ok, okListener);
        }
        if (!TextUtils.isEmpty(cancel)) {
            dlg.setButton(DialogInterface.BUTTON_NEGATIVE, cancel,
                    cancelListener);
        }
        dlg.show();
        dlg.setCancelable(false);
        managerDialog = dlg;
    }

    public void dismissDialog() {
        if (managerDialog != null && managerDialog.isShowing()) {
            managerDialog.dismiss();
        }
    }
}
