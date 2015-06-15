package com.youxing.duola.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.youxing.common.app.YXActivity;
import com.youxing.duola.R;
import com.youxing.duola.views.TitleBarButton;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class DLActivity extends YXActivity {

    protected TextView titleTv;
    protected TitleBarButton titleLeftBtn;
    protected TitleBarButton titleRightBtn;

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
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.layout_custom_titlebar);
            titleLeftBtn = (TitleBarButton) findViewById(R.id.title_left_btn);
            titleRightBtn = (TitleBarButton) findViewById(R.id.title_right_btn);
            titleTv = (TextView) findViewById(android.R.id.title);
            setTitle(getTitle());
        }
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
}
