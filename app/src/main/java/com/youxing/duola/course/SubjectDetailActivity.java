package com.youxing.duola.course;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.course.views.BuyNoticeItem;
import com.youxing.duola.course.views.CourseReviewListItem;
import com.youxing.duola.course.views.SubjectDetailCourseItem;
import com.youxing.duola.course.views.SubjectDetailHeaderItem;
import com.youxing.duola.model.Course;
import com.youxing.duola.model.Sku;
import com.youxing.duola.model.SubjectDetailModel;
import com.youxing.duola.utils.PriceUtils;
import com.youxing.duola.views.SimpleListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/3/4.
 */
public class SubjectDetailActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private String id;
    private SubjectDetailModel model;

    private Adapter adapter;
    private TextView priceTv;
    private TextView unitTv;
    private TextView chooseTv;
    private Button buyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        priceTv = (TextView) findViewById(R.id.priceTv);
        unitTv = (TextView) findViewById(R.id.unitTv);
        chooseTv = (TextView) findViewById(R.id.chooseTv);
        buyBtn = (Button) findViewById(R.id.buyBtn);
        buyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (model == null) {
                    return;
                }
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://fillorder?id=" +
                        model.getData().getSubject().getId())));
            }

        });

        id = getIntent().getData().getQueryParameter("id");

        adapter = new Adapter(this);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        requestData();
    }

    private void requestData() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
        HttpService.get(Constants.domain() + "/v2/subject", params, CacheType.DISABLE, SubjectDetailModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = (SubjectDetailModel) response;
                adapter.notifyDataSetChanged();
                setTitle(model.getData().getSubject().getTitle());
                setBuyView();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                showDialog(SubjectDetailActivity.this, error.getErrmsg());
            }
        });
    }

    private void setBuyView() {
        if (model == null) {
            return;
        }

        // TODO 单位、说明如何传
        Sku sku = model.getData().getSubject().getCheapestSku();
        if (sku == null) {
            return;
        }
        priceTv.setText(PriceUtils.formatPriceString(sku.getPrice()));
        unitTv.setText("起／月");
        chooseTv.setText("任选两门");

        if (model.getData().getSubject().getStatus() == 1) {
            buyBtn.setEnabled(true);
            buyBtn.setText("立即抢购");

        } else {
            buyBtn.setEnabled(false);
            buyBtn.setText("已售完");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        int section = indexPath.section;
        int row = indexPath.row;
        if (section == 0) {
            if (row > 0) {
                Course course = model.getData().getCourses().getList().get(row - 1);
                startActivity("duola://coursedetail?id=" + course.getId());
            }
        } else if (section == 1 && model.getData().getComments() != null &&
                model.getData().getComments().getList().size() > 0) {
            if (row == 0) {
                startActivity("duola://reviewlist?subjectId=" + SubjectDetailActivity.this.id);
            }
        }

    }


    class Adapter extends GroupStyleAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            if (model != null) {
                if (model.getData().getComments() != null && model.getData().getComments().getList().size() > 0) {
                    return 3;
                }
                return 2;
            }
            return 0;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 1 + model.getData().getCourses().getList().size();

            } else if (section == 1 && model.getData().getComments() != null &&
                    model.getData().getComments().getList().size() > 0) {
                return 2;

            } else {
                return 1;
            }
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View cell = null;
            if (section == 0) {
                if (row == 0) {
                    SubjectDetailHeaderItem item = SubjectDetailHeaderItem.create(SubjectDetailActivity.this);
                    item.setData(model.getData().getSubject());
                    cell = item;

                } else {
                    SubjectDetailCourseItem item = SubjectDetailCourseItem.create(SubjectDetailActivity.this);
                    item.setData(model.getData().getCourses().getList().get(row - 1));
                    cell = item;
                }

            } else if (section == 1 && model.getData().getComments() != null &&
                    model.getData().getComments().getList().size() > 0) {
                if (row == 0) {
                    SimpleListItem item = SimpleListItem.create(SubjectDetailActivity.this);
                    item.setTitle("用户评价");
                    item.setShowArrow(true);
                    cell = item;

                } else {
                    CourseReviewListItem view = CourseReviewListItem.create(SubjectDetailActivity.this);
                    view.setData(model.getData().getComments().getList().get(row - 1));
                    cell = view;
                }

            } else {
                BuyNoticeItem view = BuyNoticeItem.create(SubjectDetailActivity.this);
                view.setData(model.getData().getSubject().getNotice());
                cell = view;
            }
            return cell;
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
