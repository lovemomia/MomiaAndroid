package com.youxing.duola.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.model.Account;
import com.youxing.common.services.account.AccountChangeListener;
import com.youxing.common.services.account.AccountService;
import com.youxing.duola.R;
import com.youxing.duola.app.DLFragment;
import com.youxing.duola.mine.views.MineHeaderView;
import com.youxing.duola.views.SimpleListItem;
import com.youxing.duola.views.TitleBar;

/**
 * Created by Jun Deng on 15/8/3.
 */
public class MineFragment extends DLFragment implements AdapterView.OnItemClickListener, AccountChangeListener {

    private View rootView;
    private boolean rebuild;

    private TitleBar titleBar;
    private ListView listView;
    private Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_mine, null);
            titleBar = (TitleBar) rootView.findViewById(R.id.titleBar);
            listView = (ListView)rootView.findViewById(R.id.listView);
            adapter = new Adapter();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
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
            titleBar.getTitleTv().setText("我的");

            AccountService.instance().addListener(this);
        }
    }

    @Override
    public void onDestroy() {
        AccountService.instance().removeListener(this);
        super.onDestroy();
    }

    @Override
    public void onAccountChange(AccountService service) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.section == 0) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://personinfo")));
        } else if (indexPath.section == 1) {
            if (indexPath.row == 0) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://myorderlist")));
            } else {
                startActivity("duola://couponlist");
            }
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(getDLActivity());
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 1;
            }
            return 2;
        }

        @Override
        public int getSectionCount() {
            return 5;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0) {
                if (AccountService.instance().isLogin()) {
                    MineHeaderView headerView = MineHeaderView.create(getActivity());
                    Account account = AccountService.instance().account();
                    headerView.getAvartaIv().setImageUrl(account.getAvatar());
                    headerView.getNameTv().setText(account.getNickName());
//                    headerView.getAgeTv().setText(account.get);
                    view = headerView;

                } else {
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_mine_header_not_login, null);
                    view.findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("duola://login"));
                            startActivity(intent);
                        }
                    });
                }

            } else {
                SimpleListItem simpleListItem = SimpleListItem.create(getActivity());
                simpleListItem.setShowArrow(true);
                if (section == 1) {
                    if (row == 0) {
                        simpleListItem.setTitle("我的订单");
                        simpleListItem.setIcon(R.drawable.ic_mine_order);

                    } else {
                        simpleListItem.setTitle("我的红包");
                        simpleListItem.setIcon(R.drawable.ic_mine_coupon);
                    }
                } else if (section == 2) {
                    if (row == 0) {
                        simpleListItem.setTitle("成为领队");
                        simpleListItem.setIcon(R.drawable.ic_mine_leader);

                    } else {
                        simpleListItem.setTitle("我要爆料");
                        simpleListItem.setIcon(R.drawable.ic_mine_recommend);
                    }
                } else if (section == 3) {
                    if (row == 0) {
                        simpleListItem.setTitle("我的收藏");
                        simpleListItem.setIcon(R.drawable.ic_mine_collect);

                    } else {
                        simpleListItem.setTitle("常用出行人");
                        simpleListItem.setIcon(R.drawable.ic_mine_people);
                    }
                } else if (section == 4) {
                    if (row == 0) {
                        simpleListItem.setTitle("意见反馈");
                        simpleListItem.setIcon(R.drawable.ic_mine_feedback);

                    } else {
                        simpleListItem.setTitle("设置");
                        simpleListItem.setIcon(R.drawable.ic_mine_setting);
                    }
                }
                view = simpleListItem;
            }
            return view;
        }

    }
}
