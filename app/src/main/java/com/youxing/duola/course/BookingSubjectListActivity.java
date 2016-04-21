package com.youxing.duola.course;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.youxing.duola.course.views.SubjectListItem;
import com.youxing.duola.model.BookingSubjectListModel;
import com.youxing.duola.views.EmptyView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/2/18.
 */
public class BookingSubjectListActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private String oid;
    private Adapter adapter;

    private List<BookingSubjectListModel.BookingSubject> dataList = new ArrayList<BookingSubjectListModel.BookingSubject>();
    private boolean isEmpty;
    private boolean isEnd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        oid = getIntent().getData().getQueryParameter("oid");

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        HttpService.abort(handler);
        super.onDestroy();
    }

    private void requestData() {
        int start = dataList.size();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("oid", oid));
        params.add(new BasicNameValuePair("start", String.valueOf(start)));
        params.add(new BasicNameValuePair("count", "20"));
        HttpService.get(Constants.domain() + "/user/bookable", params, CacheType.DISABLE, BookingSubjectListModel.class, handler);
    }

    private RequestHandler handler = new RequestHandler() {
        @Override
        public void onRequestFinish(Object response) {
            BookingSubjectListModel model = (BookingSubjectListModel) response;
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
            showDialog(BookingSubjectListActivity.this, error.getErrmsg());
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
        if (item instanceof BookingSubjectListModel.BookingSubject) {
            BookingSubjectListModel.BookingSubject bs = (BookingSubjectListModel.BookingSubject) item;
            if (bs.getCourseId() > 0) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://book?id=" +
                        bs.getCourseId() + "&pid=" + bs.getPackageId())));
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://bookablecourselist?id=" +
                        bs.getSubjectId() + "&pid=" + bs.getPackageId())));
            }
        }
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
                EmptyView emptyView = EmptyView.create(BookingSubjectListActivity.this);
                emptyView.setMessage("您还没有待约课程包哦~");
                view = emptyView;

            } else if (item == LOADING) {
                requestData();
                view = getLoadingView(parent, convertView);

            } else {
                BookingSubjectListModel.BookingSubject subject = (BookingSubjectListModel.BookingSubject) item;
                SubjectListItem listItem = SubjectListItem.create(BookingSubjectListActivity.this);
                listItem.setData(subject);
                view = listItem;
            }
            return view;
        }

    }

}
