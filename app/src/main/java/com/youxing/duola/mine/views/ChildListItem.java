package com.youxing.duola.mine.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.model.Child;
import com.youxing.common.views.CircleImageView;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 16/4/6.
 */
public class ChildListItem extends LinearLayout implements View.OnClickListener {

    private TextView nameTv;
    private TextView ageTv;
    private CircleImageView avatarIv;
    private View editBtn;

    private Child child;

    public ChildListItem(Context context) {
        super(context);
    }

    public ChildListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ChildListItem create(Context context) {
        return (ChildListItem) LayoutInflater.from(context).inflate(R.layout.layout_child_list_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nameTv = (TextView) findViewById(R.id.name);
        ageTv = (TextView) findViewById(R.id.age);
        avatarIv = (CircleImageView) findViewById(R.id.avatar);
        avatarIv.setDefaultImageResId(R.drawable.ic_default_avatar);
        editBtn = (View) findViewById(R.id.edit);
        editBtn.setOnClickListener(this);
    }

    public void setData(Child child) {
        this.child = child;
        nameTv.setText(child.getName());
        ageTv.setText(child.getAge());
        avatarIv.setImageUrl(child.getAvatar());
    }

    @Override
    public void onClick(View v) {
        if (child != null) {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://childinfo?cid=" + child.getId())));
        }
    }
}
