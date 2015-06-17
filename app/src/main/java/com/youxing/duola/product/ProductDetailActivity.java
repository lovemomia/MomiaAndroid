package com.youxing.duola.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.duola.R;
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.product.views.ProductDetailHeaderView;
import com.youxing.duola.product.views.ProductDetailInfoView;
import com.youxing.duola.product.views.ProductDetailPartersView;

/**
 * 活动详情
 *
 * Created by Jun Deng on 15/6/11.
 */
public class ProductDetailActivity extends DLActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setTitleRightButton(R.drawable.ic_action_collect, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.share).setOnClickListener(this);
        findViewById(R.id.buy).setOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new Adapter());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.share) {


        } else if (v.getId() == R.id.buy) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://ordersubmit")));
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(ProductDetailActivity.this);
        }

        @Override
        public int getSectionCount() {
            return 4;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0 || section == 1) {
                return 1;
            } else if (section == 2) {
                return 3;
            } else if (section == 3) {
                return 2;
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0) {
                view = ProductDetailHeaderView.create(ProductDetailActivity.this);
                ((ProductDetailHeaderView)view).setData();
            } else if (section == 1) {
                view = ProductDetailPartersView.create(ProductDetailActivity.this);
            } else if (section == 2) {
                ProductDetailInfoView infoView = ProductDetailInfoView.create(ProductDetailActivity.this);
                if (row == 0) {
                    infoView.setIcon(R.drawable.ic_umbrella);
                    infoView.setTitle("适合2-6岁儿童");
                } else if (row == 1) {
                    infoView.setIcon(R.drawable.ic_alarm);
                    infoView.setTitle("2015.6.8 ~ 2015.10.1");
                } else if (row == 2) {
                    infoView.setIcon(R.drawable.ic_address);
                    infoView.setTitle("上海市浦东新区上南路168号");
                }
                view = infoView;

            } else if (section == 3) {
                if (row == 0) {
                    TextView textView = (TextView) LayoutInflater.from(ProductDetailActivity.this)
                            .inflate(R.layout.layout_product_detail_text, null);
                    textView.setText("孩子对世界充满好奇，博物馆的信息却太过繁杂，爸妈也不知从何讲起，肿么办？\n" +
                            "麦淘搜罗北京12家博物馆，打造“玩转博物馆”系列活动，专项活动+专家讲解，拿上“玩转博物馆”护照，到各个博物馆盖章，集齐12个独家印章就可以召唤神龙啦~\n" +
                            "“玩转博物馆”第1站，来中国古动物馆和大恐龙玩，拨开时间的迷雾，去看一看地球曾经的霸主。");
                    view = textView;

                } else {
                    view = LayoutInflater.from(ProductDetailActivity.this)
                            .inflate(R.layout.layout_product_detail_more, null);
                }
            }
            return view;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            return super.getViewForSection(convertView, parent, section);
        }

        @Override
        public int getHeightForSectionView(int section) {
            if (section == 0) {
                return 0;
            }
            return super.getHeightForSectionView(section);
        }
    }

}

