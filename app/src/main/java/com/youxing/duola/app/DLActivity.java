package com.youxing.duola.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.youxing.common.app.YXActivity;
import com.youxing.duola.R;
import com.youxing.duola.home.HomeActivity;
import com.youxing.duola.views.TitleBarButton;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class DLActivity extends YXActivity {

    protected TextView titleTv;
    protected TitleBarButton titleLeftBtn;
    protected TitleBarButton titleRightBtn;

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

            titleLeftBtn = (TitleBarButton) findViewById(R.id.title_left_btn);
            titleRightBtn = (TitleBarButton) findViewById(R.id.title_right_btn);
            titleTv = (TextView) findViewById(android.R.id.title);
            setTitle(getTitle());
            if (!(this instanceof HomeActivity)) {
                setTitleLeftButton(R.drawable.ic_action_back, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }
    }

    public boolean isDarkTitleBar() {
        return false;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (titleTv != null) {
            titleTv.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        if (titleTv != null) {
            titleTv.setText(titleId);
        }
    }

    public void setTitleLeftButton(int imageResId, View.OnClickListener listener) {
        titleLeftBtn.setIcon(imageResId);
        titleLeftBtn.setOnClickListener(listener);
    }

    public void setTitleRightButton(int imageResId, View.OnClickListener listener) {
        titleRightBtn.setIcon(imageResId);
        titleRightBtn.setOnClickListener(listener);
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
        }
        wm.addView(loadingView, params);
    }

    public void dismissLoading() {
        if (loadingView != null) {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(loadingView);
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
