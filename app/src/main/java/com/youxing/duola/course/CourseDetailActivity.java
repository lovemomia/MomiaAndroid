package com.youxing.duola.course;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
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
import com.youxing.duola.course.views.CourseDetailContentItem;
import com.youxing.duola.course.views.CourseDetailHeaderItem;
import com.youxing.duola.course.views.CourseDetailPlaceItem;
import com.youxing.duola.course.views.CourseDetailTabItem;
import com.youxing.duola.course.views.CourseDetailTagsItem;
import com.youxing.duola.course.views.CourseReviewListItem;
import com.youxing.duola.model.Course;
import com.youxing.duola.model.CourseDetailModel;
import com.youxing.duola.views.SimpleListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/3/4.
 */
public class CourseDetailActivity extends SGActivity implements CourseDetailTabItem.OnTabChangeListener {

    private String id;
    private Course model;

    private Toolbar titleBar;
    private Adapter adapter;
    private TextView priceTv;
    private TextView unitTv;
    private TextView chooseTv;
    private Button buyBtn;

    private int tabIndex;

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
                
            }

        });

        id = getIntent().getData().getQueryParameter("id");

        adapter = new Adapter(this);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

//        titleBar = (Toolbar) findViewById(R.id.titleBar);
//        setSupportActionBar(titleBar);

        requestData();
    }

    private void requestData() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
        HttpService.get(Constants.domain() + "/v2/course", params, CacheType.DISABLE, CourseDetailModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = ((CourseDetailModel) response).getData();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                showDialog(CourseDetailActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onTabChanged(int index) {
        tabIndex = index;
        adapter.notifyDataSetChanged();
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            if (model != null) {
                return 3;
            }
            return 0;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 2;
            } else if (section == 1) {
                if (model.getPlace() == null) {
                    return 1;
                }
                return 2;
            } else if (section == 2) {
                if (tabIndex == 0 || tabIndex == 1) {
                    return 2;
                } else {
                    //TODO comments, load next page
                    return 1 + model.getComments().getList().size();
                }
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View cell = null;
            if (section == 0) {
                if (row == 0) {
                    CourseDetailHeaderItem view = CourseDetailHeaderItem.create(CourseDetailActivity.this);
                    view.setData(model);
                    cell = view;

                } else {
                    CourseDetailTagsItem view = CourseDetailTagsItem.create(CourseDetailActivity.this);
                    String[] tags = {"适合" + model.getAge(), model.getJoined() + "人参加"};
                    view.setData(tags);
                    cell = view;
                }

            } else if (section == 1) {
                if (row == 0) {
                    SimpleListItem view = SimpleListItem.create(CourseDetailActivity.this);
                    view.setTitle("课程表");
                    view.setShowArrow(true);
                    cell = view;

                } else {
                    CourseDetailPlaceItem view = CourseDetailPlaceItem.create(CourseDetailActivity.this);
                    view.setData(model.getPlace());
                    cell = view;
                }
            } else if (section == 2) {
                if (row == 0) {
                    CourseDetailTabItem view = CourseDetailTabItem.create(CourseDetailActivity.this);
                    view.setListener(CourseDetailActivity.this);
                    view.setIndex(tabIndex);
                    cell = view;
                } else {
                    if (tabIndex == 0) {
                        CourseDetailContentItem view = CourseDetailContentItem.create(CourseDetailActivity.this);
                        view.setData(model);
                        cell = view;

                    } else if (tabIndex == 1) {
                        BuyNoticeItem view = BuyNoticeItem.create(CourseDetailActivity.this);
                        view.setData(model.getSubjectNotice());
                        cell = view;

                    } else {
                        CourseReviewListItem view = CourseReviewListItem.create(CourseDetailActivity.this);
                        view.setData(model.getComments().getList().get(row - 1));
                        cell = view;
                    }
                }
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
