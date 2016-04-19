package com.youxing.duola.course.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.model.Child;
import com.youxing.common.views.CircleImageView;
import com.youxing.duola.R;

/**
 * Created by Jun Deng on 16/4/6.
 */
public class BookConfirmChildListItem extends LinearLayout {

    private TextView nameTv;
    private TextView ageTv;
    private CircleImageView avatarIv;

    private Child child;

    public BookConfirmChildListItem(Context context) {
        super(context);
    }

    public BookConfirmChildListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static BookConfirmChildListItem create(Context context) {
        return (BookConfirmChildListItem) LayoutInflater.from(context).inflate(R.layout.layout_book_confirm_child_list_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nameTv = (TextView) findViewById(R.id.name);
        ageTv = (TextView) findViewById(R.id.age);
        avatarIv = (CircleImageView) findViewById(R.id.avatar);
        avatarIv.setDefaultImageResId(R.drawable.ic_default_avatar);
    }

    public void setData(Child child) {
        this.child = child;
        nameTv.setText(child.getName());
        ageTv.setText(child.getAge());
        avatarIv.setImageUrl(child.getAvatar());
    }
}

