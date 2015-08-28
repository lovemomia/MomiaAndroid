package com.youxing.duola.product.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.CircularImage;
import com.youxing.duola.R;
import com.youxing.duola.model.PlayFellowModel;

/**
 * Created by Jun Deng on 15/8/28.
 */
public class PlayFellowItemView extends LinearLayout {

    private CircularImage avatarIv;
    private TextView nameTv;
    private TextView childTv;

    public PlayFellowItemView(Context context) {
        this(context, null);
    }

    public PlayFellowItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static PlayFellowItemView create(Context context) {
        return (PlayFellowItemView) LayoutInflater.from(context).inflate(R.layout.layout_playfellow_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        avatarIv = (CircularImage) findViewById(R.id.avatar);
        avatarIv.setDefaultImageResId(R.drawable.ic_avatar_default);
        nameTv = (TextView) findViewById(R.id.name);
        childTv = (TextView) findViewById(R.id.child);
    }

    public void setData(PlayFellowModel.PlayFellowPerson person) {
        avatarIv.setImageUrl(person.getAvatar());
        nameTv.setText(person.getNickName());
        StringBuilder sb = new StringBuilder();
        if (person.getChildren() != null) {
            for (String child : person.getChildren()) {
                sb.append(child).append("  ");
            }
        }
        childTv.setText(sb.toString());
    }

}
