package com.youxing.duola.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.youxing.common.app.YXActivity;
import com.youxing.duola.R;
import com.youxing.duola.RootTabActivity;
import com.youxing.duola.views.TitleBar;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class DLActivity extends YXActivity {

    protected TitleBar titleBar;

    private View loadingView;
    private Dialog managerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(titleFeatureId());
    }

    protected int titleFeatureId() {
        return Window.FEATURE_CUSTOM_TITLE;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (titleFeatureId() == Window.FEATURE_CUSTOM_TITLE) {
            if (isDarkTitleBar()) {
                getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                        R.layout.layout_custom_titlebar_dark);
            } else {
                getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                        R.layout.layout_custom_titlebar_light);
            }

            titleBar = (TitleBar) findViewById(R.id.titleBar);
            setTitle(getTitle());
            if (!(this instanceof RootTabActivity)) {
                setTitleLeftButton(R.drawable.ic_action_back, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTitleBackClicked();
                    }
                });
            }
        }
    }

    public boolean isDarkTitleBar() {
        return false;
    }

    public void onTitleBackClicked() {
        finish();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        titleBar.getTitleTv().setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        titleBar.getTitleTv().setText(titleId);
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public void setTitleLeftButton(int imageResId, View.OnClickListener listener) {
        titleBar.getLeftBtn().setIcon(imageResId);
        titleBar.getLeftBtn().setOnClickListener(listener);
    }

    public void setTitleRightButton(int imageResId, View.OnClickListener listener) {
        titleBar.getRightBtn().setIcon(imageResId);
        titleBar.getRightBtn().setOnClickListener(listener);
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

    public void showLoading() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                0,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (loadingView == null) {
            loadingView = new ProgressBar(this);
        } else {
            wm.removeView(loadingView);
        }
        wm.addView(loadingView, params);
    }

    public void dismissLoading() {
        if (loadingView != null) {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(loadingView);
            loadingView = null;
        }
    }

    public void showLoadingDialog(Context context, String msg,
                                     DialogInterface.OnCancelListener cancelListener) {
        dismissDialog();

        ProgressDialog loadingDialog = new ProgressDialog(context);

        String message = msg;
        String defaultMsg = "正在加载中，请稍候...";
        if (TextUtils.isEmpty(message)) {
            message = defaultMsg;
        }
        loadingDialog.setMessage(message);
        loadingDialog.setOnCancelListener(cancelListener);
        loadingDialog.show();
        managerDialog = loadingDialog;
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
        if (managerDialog != null) {
            managerDialog.dismiss();
        }
    }
}
