package com.youxing.duola.course;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.youxing.common.adapter.BasicAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.course.views.CourseReviewListItem;
import com.youxing.duola.model.ReviewList;
import com.youxing.duola.model.ReviewListModel;
import com.youxing.duola.views.EmptyView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/3/9.
 */
public class ReviewListActivity extends SGActivity {

    private String courseId;
    private String subjectId;
    private Adapter adapter;

    private List<ReviewList.Review> dataList = new ArrayList<ReviewList.Review>();
    private boolean isEmpty;
    private boolean isEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        courseId = getIntent().getData().getQueryParameter("courseId");
        subjectId = getIntent().getData().getQueryParameter("subjectId");

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
    }

    private void requestData() {
        int start = dataList.size();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", TextUtils.isEmpty(subjectId) ? courseId : subjectId));
        params.add(new BasicNameValuePair("start", String.valueOf(start)));
        params.add(new BasicNameValuePair("count", "20"));
        String path = TextUtils.isEmpty(subjectId) ? "/course/comment/list" : "/subject/comment/list";
        HttpService.get(Constants.domain() + path, params, CacheType.DISABLE, ReviewListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                ReviewListModel model = (ReviewListModel) response;
                dataList.addAll(model.getData().getList());
                if (model.getData().getNextIndex() == 0 || model.getData().getTotalCount() <= dataList.size()) {
                    isEnd = true;
                }
                if (dataList.size() == 0) {
                    isEmpty = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                showDialog(ReviewListActivity.this, error.getErrmsg());
            }
        });
    }

    class Adapter extends BasicAdapter {

        public Adapter() {
        }

        @Override
        public int getCount() {
            if (isEmpty) {
                return 1;
            }
            if (isEnd) {
                return dataList.size();
            }
            return dataList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (isEmpty) {
                return EMPTY;
            }
            if (position < dataList.size()) {
                return dataList.get(position);
            }
            return LOADING;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object item = getItem(position);
            View view = null;
            if (item == EMPTY) {
                EmptyView emptyView = EmptyView.create(ReviewListActivity.this);
                emptyView.setMessage("还没有人评价哦～");
                view = emptyView;

            } else if (item == LOADING) {
                requestData();
                view = getLoadingView(parent, convertView);

            } else {
                ReviewList.Review review = (ReviewList.Review) item;
                CourseReviewListItem listItem = CourseReviewListItem.create(ReviewListActivity.this);
                listItem.setData(review);
                view = listItem;
            }
            return view;
        }

    }
}
