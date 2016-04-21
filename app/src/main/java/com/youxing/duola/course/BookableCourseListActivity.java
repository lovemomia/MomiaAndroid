package com.youxing.duola.course;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.BasicAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.course.views.CourseListItem;
import com.youxing.duola.model.Course;
import com.youxing.duola.model.CourseListModel;
import com.youxing.duola.views.EmptyView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 可选课程
 *
 * Created by Jun Deng on 16/2/22.
 */
public class BookableCourseListActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private String id;
    private String pid;
    private Adapter adapter;

    private List<Course> dataList = new ArrayList<Course>();
    private boolean isEmpty;
    private boolean isEnd;

    private RequestHandler handler = new RequestHandler() {
        @Override
        public void onRequestFinish(Object response) {
            CourseListModel model = (CourseListModel) response;
            dataList.addAll(model.getData().getCourses().getList());
            if (model.getData().getCourses().getNextIndex() == 0 || model.getData().getCourses().getTotalCount() <= dataList.size()) {
                isEnd = true;
            }
            if (dataList.size() == 0) {
                isEmpty = true;
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRequestFailed(BaseModel error) {
            showDialog(BookableCourseListActivity.this, error.getErrmsg());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        id = getIntent().getData().getQueryParameter("id");
        pid = getIntent().getData().getQueryParameter("pid");

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void requestData() {
        int start = dataList.size();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("pid", pid));
        params.add(new BasicNameValuePair("start", String.valueOf(start)));
        params.add(new BasicNameValuePair("count", "20"));
        HttpService.get(Constants.domain() + "/subject/course", params, CacheType.DISABLE, CourseListModel.class, handler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < dataList.size()) {
            Course course = dataList.get(position);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://book?id=" +
                    course.getId() + "&pid=" + pid)));
        }
    }

    @Override
    protected void onDestroy() {
        HttpService.abort(handler);
        super.onDestroy();
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
                EmptyView emptyView = EmptyView.create(BookableCourseListActivity.this);
                emptyView.setMessage("没有可选课程，尽请期待~");
                view = emptyView;

            } else if (item == LOADING) {
                requestData();
                view = getLoadingView(parent, convertView);

            } else {
                Course course = (Course) item;
                CourseListItem listItem = CourseListItem.create(BookableCourseListActivity.this);
                listItem.setData(course);
                view = listItem;
            }
            return view;
        }

    }

}
