package com.youxing.duola.product;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
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
import com.youxing.duola.views.ShareDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动详情
 *
 * Created by Jun Deng on 15/6/11.
 */
public class ProductDetailActivity extends DLActivity implements View.OnClickListener,
        RequestHandler, AdapterView.OnItemClickListener {

    private String id;
    private Product product;

    private TextView buyBtn;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        id = getIntent().getData().getQueryParameter("id");

        setTitleRightButton(R.drawable.ic_action_collect, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product == null) {
                    return;
                }
                if (product.isFavored()) {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("id", String.valueOf(product.getId())));
                    HttpService.post(Constants.domain() + "/product/unfavor", params, BaseModel.class, new RequestHandler() {
                        @Override
                        public void onRequestFinish(BaseModel response) {
                            product.setFavored(false);
                            getTitleBar().getRightBtn().setIcon(R.drawable.ic_action_collect);
                        }

                        @Override
                        public void onRequestFailed(BaseModel error) {
                        }
                    });

                } else {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("id", String.valueOf(product.getId())));
                    HttpService.post(Constants.domain() + "/product/favor", params, BaseModel.class, new RequestHandler() {
                        @Override
                        public void onRequestFinish(BaseModel response) {
                            product.setFavored(true);
                            getTitleBar().getRightBtn().setIcon(R.drawable.ic_action_collected);
                        }

                        @Override
                        public void onRequestFailed(BaseModel error) {
                        }
                    });
                }
            }
        });

        findViewById(R.id.share).setOnClickListener(this);
        buyBtn = (TextView) findViewById(R.id.buy);
        buyBtn.setOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

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
            if (product != null) {
                ShareDialog shareDialog = new ShareDialog(this, product.getUrl(),
                        product.getTitle(), product.getAbstracts(), product.getThumb());
                shareDialog.show();
            }

        } else if (v.getId() == R.id.buy) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://fillorder?id=" + id)));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (product == null) {
            return;
        }
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.section == 2 && indexPath.row == 0 && product.getCustomers().getAvatars() != null
                && product.getCustomers().getAvatars().size() > 0) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("duola://productplayfellow?id=" + product.getId())));
        }
    }

    @Override
    public void onRequestFinish(BaseModel response) {
        dismissDialog();

        product = ((ProductModel)response).getData();
        if (product.isSoldOut() || !product.isOpened()) {
            if (product.isSoldOut()) {
                buyBtn.setText("报名人数已满");
            } else {
                buyBtn.setText("报名已结束");
            }
            buyBtn.setEnabled(false);
        }
        if (product.isFavored()) {
            getTitleBar().getRightBtn().setIcon(R.drawable.ic_action_collected);
        } else {
            getTitleBar().getRightBtn().setIcon(R.drawable.ic_action_collect);
        }
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
                            ((ProductDetailPartersView) view).setData(product);
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

