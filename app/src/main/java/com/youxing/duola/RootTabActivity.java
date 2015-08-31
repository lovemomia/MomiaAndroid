package com.youxing.duola;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.home.HomeFragment;
import com.youxing.duola.mine.MineFragment;

public class RootTabActivity extends DLActivity {

    private FragmentTabHost tabHost;

    @Override
    protected int titleFeatureId() {
        return Window.FEATURE_NO_TITLE;
    }

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
        tabHost.addTab(
                tabHost.newTabSpec("mine").setIndicator(
                        createTabItem("我的",
                                R.drawable.ic_tab_mine)),
                MineFragment.class, null);

        // 个推
        PushManager.getInstance().initialize(this.getApplicationContext());

        // Umeng
        MobclickAgent.updateOnlineConfig(this);
        UmengUpdateAgent.silentUpdate(this);
    }

    private View createTabItem(String name, int iconRes) {
        View root = getLayoutInflater().inflate(R.layout.layout_tab_item, null);
        ImageView iconView = (ImageView) root.findViewById(R.id.tab_icon);
        iconView.setImageResource(iconRes);

        TextView nameView = (TextView) root.findViewById(R.id.tab_text);
        nameView.setText(name);

        return root;
    }

}
