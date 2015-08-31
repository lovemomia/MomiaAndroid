package com.youxing.duola.app;

import com.umeng.analytics.MobclickAgent;
import com.youxing.common.app.YXFragment;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class DLFragment extends YXFragment {

    public DLActivity getDLActivity() {
        return (DLActivity)getActivity();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }

}
