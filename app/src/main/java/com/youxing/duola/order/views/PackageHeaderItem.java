package com.youxing.duola.order.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;

/**
 * Created by Jun Deng on 15/8/11.
 */
public class PackageHeaderItem extends LinearLayout {

    private TextView packageTv;

    public PackageHeaderItem(Context context) {
        this(context, null);
    }

    public PackageHeaderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static PackageHeaderItem create(Context context) {
        return (PackageHeaderItem) LayoutInflater.from(context).inflate(R.layout.layout_package_header, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        packageTv = (TextView) findViewById(R.id.packageTv);
    }

    public TextView getPackageTv() {
        return packageTv;
    }
}
