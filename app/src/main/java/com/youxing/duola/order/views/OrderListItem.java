package com.youxing.duola.order.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.duola.R;
import com.youxing.duola.model.Order;
import com.youxing.duola.utils.PriceUtils;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class OrderListItem extends LinearLayout implements View.OnClickListener {

    private YXNetworkImageView iconIv;
    private TextView titleTv;
    private TextView priceTv;
    private TextView peopleTv;
    private TextView numberTv;
    private Button eventBtn;

    private Order order;

    public OrderListItem(Context context) {
        super(context);
    }

    public OrderListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (YXNetworkImageView) findViewById(R.id.icon);
        iconIv.setDefaultImageResId(R.drawable.bg_default_image);
        titleTv = (TextView) findViewById(R.id.title);
        priceTv = (TextView) findViewById(R.id.price);
        peopleTv = (TextView) findViewById(R.id.people);
        numberTv = (TextView) findViewById(R.id.number);
        eventBtn = (Button) findViewById(R.id.event);
        eventBtn.setOnClickListener(this);
    }

    public static OrderListItem create(Context context) {
        return (OrderListItem) LayoutInflater.from(context).inflate(R.layout.layout_order_list_item, null);
    }

    @Override
    public void onClick(View v) {
        if (order == null) {
            return;
        }

        if (order.getStatus() == 2) {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("duola://cashpay?order=" + JSON.toJSONString(order))));


        } else if (order.getStatus() >= 5) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("duola://refundinfo"));
            intent.putExtra("order", order);
            getContext().startActivity(intent);

        }
//        else if (order.getBookingStatus() == 1) {
//            getContext().startActivity(new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("duola://bookingsubjectlist?oid=" + order.getId())));
//        }
        else if (order.isCanRefund()) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("duola://applyrefund"));
            intent.putExtra("order", order);
            getContext().startActivity(intent);
        }
    }

    public void setData(Order order) {
        this.order = order;

        iconIv.setImageUrl(order.getCover());
        titleTv.setText(order.getTitle());
        priceTv.setText("数量：" + order.getCount());
        peopleTv.setText(statusStr(order));
        numberTv.setText("合计：￥" + PriceUtils.formatPriceString(order.getTotalFee()));
        if (order.getStatus() == 2) {
            eventBtn.setText("付款");
            eventBtn.setVisibility(View.VISIBLE);

        } else if (order.getStatus() == 5 || order.getStatus() == 7 ) {
            eventBtn.setText("退款中");
            eventBtn.setVisibility(View.VISIBLE);

        }  else if (order.getStatus() == 6) {
            eventBtn.setText("已退款");
            eventBtn.setVisibility(View.VISIBLE);

        }
//        else if (order.getBookingStatus() == 1) {
//            eventBtn.setText("预约");
//            eventBtn.setVisibility(View.VISIBLE);
//
//        }
        else if (order.isCanRefund()) {
            eventBtn.setText("退款");
            eventBtn.setVisibility(View.VISIBLE);
            eventBtn.setBackgroundResource(R.drawable.btn_shape_green);

        } else {
            eventBtn.setVisibility(View.GONE);
        }
    }

//    public void setData(Order order, boolean showEventBtn) {
//        setData(order);
//        eventBtn.setVisibility(showEventBtn ? View.VISIBLE : View.GONE);
//    }

    private String statusStr(Order order) {
        if (order.getStatus() == 2) {
            return "未付款";
        }

        int bookStatus = order.getBookingStatus();
        if (bookStatus == 1) {
            return "待预约";
        } else if (bookStatus == 2) {
            return "待上课";
        } else if (bookStatus == 3) {
            return "已上课";
        }
        return "未付款";
    }

}
