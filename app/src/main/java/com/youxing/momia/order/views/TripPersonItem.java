package com.youxing.momia.order.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.momia.R;
import com.youxing.momia.model.Person;

/**
 * Created by Jun Deng on 15/6/11.
 */
public class TripPersonItem extends LinearLayout {

    private ImageView editIv;
    private TextView nameTv;
    private TextView propertyTv;
    private TextView dtypeTv;
    private TextView dnumberTv;
    private CheckBox checkBox;

    public TripPersonItem(Context context) {
        this(context, null);
    }

    public TripPersonItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        editIv = (ImageView)findViewById(R.id.edit);
        nameTv = (TextView)findViewById(R.id.name);
        propertyTv = (TextView)findViewById(R.id.property);
        dtypeTv = (TextView)findViewById(R.id.dtype);
        dnumberTv = (TextView)findViewById(R.id.dnumber);
        checkBox = (CheckBox)findViewById(R.id.checkbox);
    }

    public void setData(Person person) {

    }

    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }
}
