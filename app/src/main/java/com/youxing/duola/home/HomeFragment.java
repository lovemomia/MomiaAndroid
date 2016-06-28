package com.youxing.duola.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.Account;
import com.youxing.common.model.BaseModel;
import com.youxing.common.model.City;
import com.youxing.common.services.account.AccountChangeListener;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.CityManager;
import com.youxing.duola.R;
import com.youxing.duola.app.SGFragment;
import com.youxing.duola.home.views.HomeEventItem;
import com.youxing.duola.home.views.HomeHeaderView;
import com.youxing.duola.home.views.HomeRecommendItem;
import com.youxing.duola.home.views.HomeSubjectContentItem;
import com.youxing.duola.home.views.HomeSubjectCoverItem;
import com.youxing.duola.home.views.HomeTitleBar;
import com.youxing.duola.home.views.HomeTopicItem;
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
        SwipeRefreshLayout.OnRefreshListener, RequestHandler, CityManager.CityChangeListener,
        AccountChangeListener {

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
        AccountService.instance().addListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupTitle();
    }

    private void setupTitle() {
        titleBar.getAvatarIv().setDefaultImageResId(R.drawable.ic_default_avatar);
        titleBar.getChildLay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("duola://personinfo");
            }
        });
        if (AccountService.instance().isLogin()) {
            Account account = AccountService.instance().account();
            titleBar.getAvatarIv().setImageUrl(account.getAvatar());
            titleBar.getNameTv().setText(account.getNickName() + " " + account.getAgeOfChild());

        } else {
            titleBar.getAvatarIv().setImageUrl("");
            titleBar.getNameTv().setText("松果亲子／点击登录");
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
        adapter.reset();
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

        ListItem item = adapter.getListItems().get(ip.section);
        if (item.type == ListItem.ITEM_TYPE_BANNEL) {
            // handler by it self

        } else if (item.type == ListItem.ITEM_TYPE_EVENT) {
            // handler by it self

        } else if (item.type == ListItem.ITEM_TYPE_SUBJECT_COVER) {
            HomeModel.HomeSubject subject = model.getData().getSubjects().get(item.index);
            startActivity("duola://subjectdetail?id=" + subject.getId());

        } else if (item.type == ListItem.ITEM_TYPE_SUBJECT_COURSES) {
            // handler by it self

        } else if (item.type == ListItem.ITEM_TYPE_TOPIC) {
            // topic
            String url = Constants.domainWeb() + "/discuss/topic?id=" + model.getData().getTopics().get(item.index).getId();
            startActivity("duola://web?url=" + url);

        } else {
            HomeModel.HomeRecommend recommend = model.getData().getRecommends().get(ip.row);
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recommend.getAction())));
        }

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

    @Override
    public void onAccountChange(AccountService service) {
        setupTitle();
    }

    class Adapter extends GroupStyleAdapter {

        private List<ListItem> listItems;

        public Adapter(Context context) {
            super(context);
        }

        public void reset() {
            if (listItems != null) {
                listItems.clear();
                listItems = null;
            }
        }

        private List<ListItem> getListItems() {
            if (listItems != null) {
                return listItems;
            }

            listItems = new ArrayList<ListItem>();

            if (hasBannel) {
                listItems.add(new ListItem(ListItem.ITEM_TYPE_BANNEL));
            }

            if (hasEvent) {
                listItems.add(new ListItem(ListItem.ITEM_TYPE_EVENT));
            }

            for (int i = 0; i < model.getData().getSubjects().size(); i++) {
                listItems.add(new ListItem(ListItem.ITEM_TYPE_SUBJECT_COVER, i));
                listItems.add(new ListItem(ListItem.ITEM_TYPE_SUBJECT_COURSES, i));
                if (i < model.getData().getTopics().size()) {
                    listItems.add(new ListItem(ListItem.ITEM_TYPE_TOPIC, i));
                }
            }

            if (model.getData().getRecommends().size() > 0) {
                listItems.add(new ListItem(ListItem.ITEM_TYPE_RECOMMEND));
            }
            return listItems;
        }

        @Override
        public int getSectionCount() {
            if (model != null) {
                return getListItems().size();
            }
            return 0;
        }

        @Override
        public int getCountInSection(int section) {
            ListItem item = getListItems().get(section);
            if (item.type == ListItem.ITEM_TYPE_RECOMMEND) {
                return model.getData().getRecommends().size();
            }
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
            ListItem item = getListItems().get(section);
            if (item.type == ListItem.ITEM_TYPE_BANNEL) {
                HomeHeaderView cell = HomeHeaderView.create(getActivity());
                cell.setData(model.getData().getBanners());
                return cell;

            } else if (item.type == ListItem.ITEM_TYPE_EVENT) {
                HomeEventItem cell = HomeEventItem.create(getActivity());
                cell.setData(model);
                return cell;

            } else if (item.type == ListItem.ITEM_TYPE_SUBJECT_COVER) {
                HomeModel.HomeSubject subject = model.getData().getSubjects().get(item.index);
                HomeSubjectCoverItem cell = HomeSubjectCoverItem.create(getActivity());
                cell.setData(subject.getCover());
                return cell;

            } else if (item.type == ListItem.ITEM_TYPE_SUBJECT_COURSES) {
                HomeModel.HomeSubject subject = model.getData().getSubjects().get(item.index);
                HomeSubjectContentItem cell = HomeSubjectContentItem.create(getActivity());
                cell.setData(subject);
                return cell;

            } else if (item.type == ListItem.ITEM_TYPE_TOPIC) {
                HomeTopicItem cell = HomeTopicItem.create(getActivity());
                cell.setData(model.getData().getTopics().get(item.index));
                return cell;

            } else {
                HomeRecommendItem cell = HomeRecommendItem.create(getActivity());
                cell.setData(model.getData().getRecommends().get(row));
                return cell;
            }
        }

    }

    @Override
    public void onDestroy() {
        HttpService.abort(this);
        CityManager.instance().removeListener(this);
        AccountService.instance().removeListener(this);
        super.onDestroy();
    }

    class ListItem {
        private static final int ITEM_TYPE_BANNEL = 1;
        private static final int ITEM_TYPE_EVENT = 2;
        private static final int ITEM_TYPE_SUBJECT_COVER = 3;
        private static final int ITEM_TYPE_SUBJECT_COURSES = 4;
        private static final int ITEM_TYPE_TOPIC = 5;
        private static final int ITEM_TYPE_RECOMMEND = 6;

        private int type;
        private int index;

        public ListItem(int type) {
            this(type, 0);
        }

        public ListItem(int type, int index) {
            this.type = type;
            this.index = index;
        }

    }

}
