package com.youxing.duola;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.chat.ChatListFragment;
import com.youxing.duola.home.HomeFragment;
import com.youxing.duola.mine.MineFragment;
import com.youxing.duola.model.IMTokenModel;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class RootTabActivity extends SGActivity implements RongIMClient.OnReceiveMessageListener, TabHost.OnTabChangeListener {

    private FragmentTabHost tabHost;
    private View groupDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabHost.addTab(
                tabHost.newTabSpec("home").setIndicator(
                        createTabItem("精选",
                                R.drawable.ic_tab_home)), HomeFragment.class,
                null);

        View groupTabItem = createTabItem("群组", R.drawable.ic_tab_group);
        groupDot = groupTabItem.findViewById(R.id.dot);
        tabHost.addTab(tabHost.newTabSpec("chatlist").setIndicator(groupTabItem), ChatListFragment.class, null);

        tabHost.addTab(
                tabHost.newTabSpec("mine").setIndicator(
                        createTabItem("我的",
                                R.drawable.ic_tab_mine)),
                MineFragment.class, null);

        if (getIntent().getData() != null) {
            String host = getIntent().getData().getHost();
            if (!TextUtils.isEmpty(host)) {
                if (host.equals("mine")) {
                    tabHost.setCurrentTab(2);
                } else if (host.equals("chatlist")) {
                    tabHost.setCurrentTab(1);
                }
            }
        }

        tabHost.setOnTabChangedListener(this);
        tabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AccountService.instance().isLogin()) {
                    AccountService.instance().login(RootTabActivity.this, new AccountService.LoginListener() {
                        @Override
                        public void onLoginSuccess() {
                            doRCIMConnect(3);
                        }

                        @Override
                        public void onLoginFailed() {
                        }
                    });
                } else {
                    tabHost.setCurrentTab(1);
                }
            }
        });

        // Umeng
        MobclickAgent.updateOnlineConfig(this);
        UmengUpdateAgent.silentUpdate(this);

        // RongCloud
        doRCIMConnect(3);
        RongIM.setOnReceiveMessageListener(this);
    }

    private View createTabItem(String name, int iconRes) {
        View root = getLayoutInflater().inflate(R.layout.layout_tab_item, null);
        ImageView iconView = (ImageView) root.findViewById(R.id.tab_icon);
        iconView.setImageResource(iconRes);

        TextView nameView = (TextView) root.findViewById(R.id.tab_text);
        nameView.setText(name);

        return root;
    }

    @Override
    protected boolean showTitleShadow() {
        return false;
    }

    private long mPrevbackPress = -1;

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - mPrevbackPress <= 1000) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再按一次返回退出松果亲子", Toast.LENGTH_SHORT).show();
        }
        mPrevbackPress = t;
    }

    private void doRCIMConnect(final int tryTime) {
        if (!AccountService.instance().isLogin()) {
            return;
        }
        String imToken = AccountService.instance().account().getImToken();
        if (imToken == null) {
            return;
        }

        RongIM.connect(AccountService.instance().account().getImToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token

                if (tryTime > 0) {
                    HttpService.post(Constants.domain() + "/im/token", null, IMTokenModel.class, new RequestHandler() {
                        @Override
                        public void onRequestFinish(Object response) {
                            IMTokenModel model = (IMTokenModel) response;
                            AccountService.instance().account().setImToken(model.getData());
                            doRCIMConnect(tryTime - 1);
                        }

                        @Override
                        public void onRequestFailed(BaseModel error) {

                        }
                    });
                }
            }

            @Override
            public void onSuccess(String userId) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    @Override
    public boolean onReceived(Message message, int i) {
        refreshDot();
        return false;
    }

    @Override
    public void onTabChanged(String tabId) {
        if (AccountService.instance().isLogin()) {
            refreshDot();
        }
    }

    private void refreshDot() {
        try {
            int unreadGroup = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP);
            if (unreadGroup > 0) {
                groupDot.setVisibility(View.VISIBLE);

            } else {
                groupDot.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
    }
}
