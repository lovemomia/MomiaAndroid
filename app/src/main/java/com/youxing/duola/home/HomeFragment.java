package com.youxing.duola.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.Account;
import com.youxing.common.model.BaseModel;
import com.youxing.common.model.City;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.CityManager;
import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;
import com.youxing.duola.app.SGFragment;
import com.youxing.duola.home.views.HomeEventItem;
import com.youxing.duola.home.views.HomeHeaderView;
import com.youxing.duola.home.views.HomeListItem;
import com.youxing.duola.home.views.HomeSubjectContentItem;
import com.youxing.duola.home.views.HomeSubjectCoverItem;
import com.youxing.duola.home.views.HomeTitleBar;
import com.youxing.duola.home.views.HomeTopicItem;
import com.youxing.duola.model.Course;
import com.youxing.duola.model.HomeModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 *
 * Created by Jun Deng on 15/8/3.
 * Refactor on 16/3/10 v1.4
 */
public class HomeFragment extends SGFragment implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, RequestHandler, CityManager.CityChangeListener {

    private View rootView;
    private boolean rebuild;

    private SwipeRefreshLayout swipeLayout;
    private HomeTitleBar titleBar;
    private ListView listView;
    private Adapter adapter;

    private HomeModel model;

    private boolean isRefresh;
    private boolean hasBannel;
    private boolean hasEvent;
    private boolean hasTopic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_home, null);

            titleBar = (HomeTitleBar) rootView.findViewById(R.id.titleBar);
            titleBar.getCityTv().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity("duola://citylist");
                }
            });

            listView = (ListView)rootView.findViewById(R.id.listView);
            listView.setOnItemClickListener(this);
            adapter = new Adapter(getContext());
            listView.setAdapter(adapter);

            swipeLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorSchemeResources(R.color.app_theme);

            rebuild = true;
        } else {
            rebuild = false;
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (rebuild) {
//            initTitle();

            requestData();
        }

        CityManager.instance().addListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccountService.instance().isLogin()) {
            Account account = AccountService.instance().account();
            titleBar.getAvatarIv().setImageUrl(account.getAvatar());
            titleBar.getNameTv().setText(account.getNickName());
            titleBar.getAgeTv().setText(account.getAgeOfChild());
        }
    }

    private void requestData() {
        if (model == null) {
            getDLActivity().showLoadingDialog(getActivity(), null, null);
        }

        String url = Constants.domain() + "/v3/index";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("start", "0"));

        HttpService.get(url, params, CacheType.DISABLE, HomeModel.class, this);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        hasBannel = false;
        hasEvent = false;
        hasTopic = false;
        requestData();
    }

    @Override
    public void onCityChanged(City newCity) {
        onRefresh();
        titleBar.getCityTv().setText(newCity.getName());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (model == null) {
            return;
        }

        GroupStyleAdapter.IndexPath ip = adapter.getIndexForPosition(position);
        int sec = ip.section;
        if (hasBannel) {
            if (sec == 0) {
                // handler by it self
                return;
            }
            sec--;
        }

        if (hasEvent) {
            if (sec == 0) {
                // handler by it self
                return;
            }
            sec--;
        }

        if (sec < model.getData().getSubjects().size() * 2) {
            HomeModel.HomeSubject subject = model.getData().getSubjects().get(sec / 2);
            if (sec % 2 == 0) {
                startActivity("duola://subjectdetail?id=" + subject.getId());
                return;

            } else {
                // handler by it self
                return;
            }
        }

        sec -= model.getData().getSubjects().size() * 2;

        if (hasTopic) {
            if (sec == 0) {
                // topic
                return;
            }
            sec--;
        }

        Course course = model.getData().getCourses().getList().get(sec);
        startActivity("duola://coursedetail?id=" + course.getId());
    }

    @Override
    public void onRequestFinish(Object response) {
        if (isRefresh) {
            isRefresh = false;
            swipeLayout.setRefreshing(false);
        }

        if (model == null) {
            getDLActivity().dismissDialog();
        }

        model = (HomeModel) response;

        if (model.getData().getBanners() != null && model.getData().getBanners().size() > 0) {
            hasBannel = true;
        } else {
            hasBannel = false;
        }

        if (model.getData().getEvents() != null && model.getData().getEvents().size() > 0) {
            hasEvent = true;
        } else {
            hasEvent = false;
        }

        if (model.getData().getTopics() != null && model.getData().getTopics().size() > 0) {
            hasTopic = true;
        } else {
            hasTopic = false;
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRequestFailed(BaseModel error) {
        getDLActivity().dismissDialog();
        if (isRefresh) {
            isRefresh = false;
            swipeLayout.setRefreshing(false);
        }
        getDLActivity().showDialog(getDLActivity(), error.getErrmsg());
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            if (model != null) {
                int count = 0;
                if (hasBannel) {
                    count++;
                }
                if (hasEvent) {
                    count++;
                }
                count += model.getData().getSubjects().size() * 2;
                if (hasTopic) {
                    count++;
                }
                count += model.getData().getCourses().getList().size();
                return count;
            }
            return 0;
        }

        @Override
        public int getCountInSection(int section) {
            return 1;
        }

        @Override
        public int getHeightForSectionView(int section) {
            if (section == 0) {
                return 0;
            }
            return super.getHeightForSectionView(section);
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View cell = null;
            int sec = section;
            if (hasBannel) {
                if (sec == 0) {
                    HomeHeaderView header = HomeHeaderView.create(getActivity());
                    header.setData(model.getData().getBanners());
                    cell = header;
                    return cell;
                }
                sec--;
            }

            if (hasEvent) {
                if (sec == 0) {
                    HomeEventItem item = HomeEventItem.create(getActivity());
                    item.setData(model);
                    cell = item;
                    return cell;
                }
                sec--;
            }

            if (sec < model.getData().getSubjects().size() * 2) {
                HomeModel.HomeSubject subject = model.getData().getSubjects().get(sec / 2);
                if (sec % 2 == 0) {
                    HomeSubjectCoverItem item = HomeSubjectCoverItem.create(getActivity());
                    item.setData(subject.getCover());
                    cell = item;
                    return cell;

                } else {
                    HomeSubjectContentItem item = HomeSubjectContentItem.create(getActivity());
                    item.setData(subject);
                    cell = item;
                    return cell;
                }
            }

            sec -= model.getData().getSubjects().size() * 2;

            if (hasTopic) {
                if (sec == 0) {
                    HomeTopicItem item = HomeTopicItem.create(getActivity());
                    item.setData(model.getData().getTopics().get(0));
                    cell = item;
                    return cell;
                }
                sec--;
            }

            HomeListItem item = HomeListItem.create(getActivity());
            item.setData(model.getData().getCourses().getList().get(sec));
            cell = item;

            return cell;
        }

    }

    @Override
    public void onDestroy() {
        HttpService.abort(this);
        CityManager.instance().removeListener(this);
        super.onDestroy();
    }

}
