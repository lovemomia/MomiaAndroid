package com.youxing.duola.course;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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
import com.youxing.duola.app.SGFragment;
import com.youxing.duola.course.views.BookedCourseListItem;
import com.youxing.duola.course.views.CourseListItem;
import com.youxing.duola.model.BookedCourseListModel;
import com.youxing.duola.model.Course;
import com.youxing.duola.views.EmptyView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的－已选课程
 * <p/>
 * Created by Jun Deng on 16/2/25.
 */
public class BookedCourseListFragment extends SGFragment implements AdapterView.OnItemClickListener,
        BookedCourseListItem.OnCourseCancelBookListener {

    public final static String INTENT_ACTION_DATA_CHANGE = "com.youxing.duola.ACTION_DATA_CHANGE";

    private String id;
    private boolean finish;

    private Adapter adapter;
    private List<Course> dataList = new ArrayList<Course>();
    private boolean isEmpty;
    private boolean isEnd;
    private long nextIndex;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(INTENT_ACTION_DATA_CHANGE)) {
                reload();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getString("id");
        finish = getArguments().getBoolean("finish");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, null);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_ACTION_DATA_CHANGE);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDetach() {
        getActivity().unregisterReceiver(receiver);
        super.onDetach();
    }

    public void refresh() {
        adapter.notifyDataSetChanged();
    }

    public void reload() {
        dataList.clear();
        isEmpty = false;
        isEnd = false;
        nextIndex = 0;

        adapter.notifyDataSetChanged();
    }

    private void requestData() {
        getDLActivity().showLoadingDialog(getActivity());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("start", String.valueOf(nextIndex)));

        String path = finish ? "/user/course/finished" : "/user/course/notfinished";

        HttpService.get(Constants.domain() + path, params, CacheType.DISABLE, BookedCourseListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                getDLActivity().dismissDialog();

                BookedCourseListModel model = (BookedCourseListModel) response;
                dataList.addAll(model.getData().getList());
                nextIndex = model.getData().getNextIndex();

                if (nextIndex == 0 || model.getData().getTotalCount() <= dataList.size()) {
                    isEnd = true;
                }
                if (dataList.size() == 0) {
                    isEmpty = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                getDLActivity().showDialog(getActivity(), error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < dataList.size()) {
            Course course = dataList.get(position);
            startActivity("duola://coursedetail?id=" + course.getId());
        }
    }

    @Override
    public void onCourseCancelBook(Course course) {
        reload();
    }

    class Adapter extends BasicAdapter {
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
                EmptyView emptyView = EmptyView.create(getActivity());
                emptyView.setMessage("还没有课程，赶紧去看看吧~");
                view = emptyView;

            } else if (item == LOADING) {
                requestData();
                view = getLoadingView(parent, convertView);

            } else {
                Course course = (Course) item;
                BookedCourseListItem listItem = BookedCourseListItem.create(getActivity());
                listItem.setData(course, finish);
                listItem.setCancelBookListener(BookedCourseListFragment.this);
                view = listItem;
            }
            return view;
        }

    }

}
