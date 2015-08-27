package com.youxing.duola.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.model.Product;
import com.youxing.duola.model.ProductModel;
import com.youxing.duola.product.views.ProductDetailContentView;
import com.youxing.duola.product.views.ProductDetailContentHeaderView;
import com.youxing.duola.product.views.ProductDetailHeaderView;
import com.youxing.duola.product.views.ProductDetailInfoView;
import com.youxing.duola.product.views.ProductDetailPartersView;
import com.youxing.duola.product.views.ProductDetailTagsView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动详情
 *
 * Created by Jun Deng on 15/6/11.
 */
public class ProductDetailActivity extends DLActivity implements View.OnClickListener, RequestHandler {

    private String id;
    private Product product;

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        id = getIntent().getData().getQueryParameter("id");

        setTitleRightButton(R.drawable.ic_action_collect, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.share).setOnClickListener(this);
        findViewById(R.id.buy).setOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);

        requestData();
    }

    private void requestData() {
        showLoadingDialog(this);

        String url = Constants.domain() + "/product";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", id));

        HttpService.get(url, params, CacheType.NORMAL, ProductModel.class, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.share) {


        } else if (v.getId() == R.id.buy) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://fillorder?id=" + id)));
        }
    }

    @Override
    public void onRequestFinish(BaseModel response) {
        dismissDialog();

        product = ((ProductModel)response).getData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestFailed(BaseModel error) {
        dismissDialog();
        showDialog(this, "对不起", error.getErrmsg(), "确定");
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(ProductDetailActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (product == null) {
                return 0;
            }
            int section = 2;
            if (product.getCustomers() != null && product.getCustomers().getAvatars() != null
                    && product.getCustomers().getAvatars().size() > 0) {
                section ++;
            }
            section += product.getContent().size();
            return section;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                if (product.getTags() != null && product.getTags().length > 0) {
                    return 2;
                }
                return 1;

            } else if (section == 1) {
                return 3;

            } else {
                return 2;
            }
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0) {
                if (row == 0) {
                    view = ProductDetailHeaderView.create(ProductDetailActivity.this);
                    ((ProductDetailHeaderView)view).setData(product);
                } else {
                    ProductDetailTagsView tagsView = ProductDetailTagsView.create(ProductDetailActivity.this);
                    tagsView.setData(product.getTags());
                    view = tagsView;
                }

            } else if (section == 1) {
                ProductDetailInfoView infoView = ProductDetailInfoView.create(ProductDetailActivity.this);
                if (row == 0) {
                    infoView.setIcon(R.drawable.ic_umbrella);
                    infoView.setTitle(product.getCrowd());

                } else if (row == 1) {
                    infoView.setIcon(R.drawable.ic_alarm);
                    infoView.setTitle(product.getScheduler());

                } else if (row == 2) {
                    infoView.setIcon(R.drawable.ic_address);
                    infoView.setTitle(product.getAddress());
                }
                view = infoView;

            } else {
                if (product.getCustomers().getAvatars() != null && product.getCustomers().getAvatars().size() > 0) {
                    if (section == 2) {
                        if (row == 0) {
                            ProductDetailContentHeaderView contentHeader = ProductDetailContentHeaderView.create(ProductDetailActivity.this);
                            contentHeader.setTitle(product.getCustomers().getText());
                            contentHeader.setArrowShow(true);
                            view = contentHeader;

                        } else {
                            view = ProductDetailPartersView.create(ProductDetailActivity.this);
                            ((ProductDetailPartersView) view).setData(product.getCustomers());
                        }

                    } else {
                        Product.ContentItem contentItem = product.getContent().get(section - 3);
                        if (row == 0) {
                            ProductDetailContentHeaderView contentHeader = ProductDetailContentHeaderView.create(ProductDetailActivity.this);
                            contentHeader.setTitle(contentItem.getTitle());
                            view = contentHeader;

                        } else {
                            ProductDetailContentView content = ProductDetailContentView.create(ProductDetailActivity.this);
                            content.setData(contentItem);
                            view = content;
                        }
                    }

                } else {
                    Product.ContentItem contentItem = product.getContent().get(section - 2);
                    if (row == 0) {
                        ProductDetailContentHeaderView contentHeader = ProductDetailContentHeaderView.create(ProductDetailActivity.this);
                        contentHeader.setTitle(contentItem.getTitle());
                        view = contentHeader;

                    } else {
                        ProductDetailContentView content = ProductDetailContentView.create(ProductDetailActivity.this);
                        content.setData(contentItem);
                        view = content;
                    }
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

