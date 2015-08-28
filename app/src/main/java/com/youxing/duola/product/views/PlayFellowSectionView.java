package com.youxing.duola.product.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.duola.R;
import com.youxing.duola.model.PlayFellowModel;

/**
 * Created by Jun Deng on 15/8/28.
 */
public class PlayFellowSectionView extends LinearLayout {

    private TextView dateTv;
    private TextView numberTv;
    private ImageView arrowIv;

    public PlayFellowSectionView(Context context) {
        this(context, null);
    }

    public PlayFellowSectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static PlayFellowSectionView create(Context context) {
        return (PlayFellowSectionView) LayoutInflater.from(context).inflate(R.layout.layout_playfellow_section, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dateTv = (TextView) findViewById(R.id.date);
        numberTv = (TextView) findViewById(R.id.number);
        arrowIv = (ImageView) findViewById(R.id.arrow);
    }

    public void setData(PlayFellowModel.PlayFellowGroup group) {
        dateTv.setText(group.getTime());
        numberTv.setText(group.getJoined());
        arrowIv.setImageResource(group.isSelected() ? R.drawable.ic_up : R.drawable.ic_down);
    }
}
