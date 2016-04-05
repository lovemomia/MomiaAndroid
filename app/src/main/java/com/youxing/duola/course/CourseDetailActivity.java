package com.youxing.duola.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.course.views.BuyNoticeItem;
import com.youxing.duola.course.views.CourseDetailContentItem;
import com.youxing.duola.course.views.CourseDetailHeaderItem;
import com.youxing.duola.course.views.CourseDetailNoticeItem;
import com.youxing.duola.course.views.CourseDetailPlaceItem;
import com.youxing.duola.course.views.CourseDetailTabItem;
import com.youxing.duola.course.views.CourseDetailTagsItem;
import com.youxing.duola.course.views.CourseDetailTeacherItem;
import com.youxing.duola.course.views.CourseReviewListItem;
import com.youxing.duola.model.Course;
import com.youxing.duola.model.CourseDetailModel;
import com.youxing.duola.utils.PriceUtils;
import com.youxing.duola.views.EmptyView;
import com.youxing.duola.views.SimpleListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/3/4.
 */
public class CourseDetailActivity extends SGActivity implements CourseDetailTabItem.OnTabChangeListener,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private String id;
    private int recommend; // 1 表示推荐，可不传或传0，表示课程包点进去的
    private Course model;

    private View titleLay;
    private Toolbar toolbar;
    private View backBtn;

    private ListView listView;
    private Adapter adapter;
    private TextView priceTv;
    private TextView unitTv;
    private TextView chooseTv;
    private Button buyBtn;
    private CourseDetailTabItem topTab;

    private int tabIndex;

    @Override
    protected boolean showTitleShadow() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }

        setContentView(R.layout.activity_course_detail);

        titleLay = findViewById(R.id.title_lay);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("课程详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);

        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(this);

        priceTv = (TextView) findViewById(R.id.priceTv);
        unitTv = (TextView) findViewById(R.id.unitTv);
        chooseTv = (TextView) findViewById(R.id.chooseTv);
        buyBtn = (Button) findViewById(R.id.buyBtn);
        buyBtn.setOnClickListener(this);

        id = getIntent().getData().getQueryParameter("id");
        String recommendStr = getIntent().getData().getQueryParameter("recommend");
        if (!TextUtils.isEmpty(recommendStr)) {
            recommend = Integer.valueOf(recommendStr);
        }

        topTab = (CourseDetailTabItem) findViewById(R.id.top_tab);
        topTab.setListener(this);
        topTab.setVisibility(View.GONE);

        adapter = new Adapter(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);

        requestData();
    }

    private void requestData() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("recommend", String.valueOf(recommend)));
        HttpService.get(Constants.domain() + "/v3/course", params, CacheType.DISABLE, CourseDetailModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = ((CourseDetailModel) response).getData();
                adapter.notifyDataSetChanged();
                setBuyView();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                showDialog(CourseDetailActivity.this, error.getErrmsg());
            }
        });
    }

    private void setBuyView() {
        if (model == null) {
            return;
        }

        if (model.isBuyable()) {
            //单次课程
            priceTv.setText(PriceUtils.formatPriceString(model.getPrice()));
            unitTv.setText("／次");
            chooseTv.setText("");

        } else {
            priceTv.setText(PriceUtils.formatPriceString(model.getCheapestSkuPrice()));
            unitTv.setText("起／" + model.getCheapestSkuTimeUnit());
            chooseTv.setText(model.getCheapestSkuDesc());
        }

        if (model.getStatus() == 1) {
            buyBtn.setEnabled(true);
            buyBtn.setText("立即抢购");

        } else {
            buyBtn.setEnabled(false);
            buyBtn.setText("名额已满");
        }
    }

    @Override
    public void onTabChanged(CourseDetailTabItem tabItem, int index) {
        topTab.setIndex(index);
        tabIndex = index;
        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(getTabPosition());
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {


    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (listView.getChildAt(0) == null) {
            return;
        }

        int tabPos = getTabPosition();
        if (firstVisibleItem >= tabPos - 2) {
            if (tabPos == 6) {
                topTab.setVisibility(View.VISIBLE);

            } else if (firstVisibleItem == tabPos - 2) {
                View poiView = listView.getChildAt(0);
                if (poiView.getHeight() + poiView.getTop() + UnitTools.dip2px(CourseDetailActivity.this, 10) <= toolbar.getHeight()) {
                    topTab.setVisibility(View.VISIBLE);
                } else {
                    topTab.setVisibility(View.GONE);
                }
            } else {
                topTab.setVisibility(View.VISIBLE);
            }

        } else if (firstVisibleItem < getTabPosition() - 2) {
            topTab.setVisibility(View.GONE);
        }

        if (firstVisibleItem > 1) {
            return;
        }

        int scrolly = view.getChildAt(0).getTop();

        if (Build.VERSION.SDK_INT >= 11) {
            float p = -scrolly / 200.0f;
            if (p > 1) {
                p = 1;
            }
            titleLay.setAlpha(p);

        }
    }

    private int getTabPosition() {
        if (model != null && model.getPlace() != null) {
            return 7;
        }
        return 6;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath ip = adapter.getIndexForPosition(position);
        if (ip.section == 1 && ip.row == 0) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://book?id=" +
                    this.id + "&onlyshow=1")));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            finish();

        } else {
            if (model == null) {
                return;
            }
            StringBuilder sb = new StringBuilder("duola://fillorder?id=" + model.getSubjectId());
            if (model.isBuyable()) {
                sb.append("&coid=" + model.getId() + "&coname=" + model.getSubject());
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString())));
        }

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
                if (tabIndex == 0) {
                    if (model.getTeachers() != null) {
                        return 3 + model.getTeachers().size();
                    }
                    return 3;

                } else if (tabIndex == 1) {
                    return 2;

                } else {
                    //TODO comments, load next page
                    if (model.getComments() == null) {
                        return 2;
                    }
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
                        if (row == 1) {
                            CourseDetailContentItem view = CourseDetailContentItem.create(CourseDetailActivity.this);
                            view.setData(model);
                            cell = view;
                        } else if (row == 2) {
                            CourseDetailNoticeItem view = CourseDetailNoticeItem.create(CourseDetailActivity.this);
                            view.setData(model.getTips());
                            cell = view;
                        } else {
                            CourseDetailTeacherItem view = CourseDetailTeacherItem.create(CourseDetailActivity.this);
                            view.setData(model.getTeachers().get(row - 3), row == 3); //只有第一个显示title
                            cell = view;
                        }

                    } else if (tabIndex == 1) {
                        BuyNoticeItem view = BuyNoticeItem.create(CourseDetailActivity.this);
                        view.setData(model.getSubjectNotice());
                        cell = view;

                    } else {
                        if (model.getComments() == null || model.getComments().getList().size() == 0) {
                            EmptyView view = EmptyView.create(CourseDetailActivity.this);
                            view.setMessage("还没有人评价哦～");
                            cell = view;

                        } else {
                            CourseReviewListItem view = CourseReviewListItem.create(CourseDetailActivity.this);
                            view.setData(model.getComments().getList().get(row - 1), 3);
                            cell = view;
                        }
                    }
                }
            }
            return cell;
        }

        @Override
        public int getBackgroundColorForRow(IndexPath indexPath) {
            if (indexPath.section == 0 && indexPath.row == 1) {
                return Color.parseColor("#f8f8f8");
            }
            return super.getBackgroundColorForRow(indexPath);
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
