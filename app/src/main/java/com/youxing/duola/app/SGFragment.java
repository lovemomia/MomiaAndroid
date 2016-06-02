package com.youxing.duola.app;

import android.os.Handler;
import android.view.KeyEvent;
import android.widget.EditText;

import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.umeng.analytics.MobclickAgent;
import com.youxing.common.app.YXFragment;
import com.youxing.duola.BuildConfig;
import com.youxing.duola.react.SGReactPackage;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class SGFragment extends YXFragment implements DefaultHardwareBackBtnHandler {

    private ReactInstanceManager mReactInstanceManager;

    public SGActivity getDLActivity() {
        return (SGActivity)getActivity();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onResume(getActivity(), this);
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onPause();
        }
    }

    public ReactInstanceManager getReactInstanceManager(String assetName, String moduleName) {
        if (mReactInstanceManager == null) {
            mReactInstanceManager = ReactInstanceManager.builder()
                    .setApplication(getActivity().getApplication())
                    .setBundleAssetName(assetName)
                    .setJSMainModuleName(moduleName)
                    .addPackage(new MainReactPackage())
                    .addPackage(new SGReactPackage())
                    .setUseDeveloperSupport(BuildConfig.DEBUG)
                    .setInitialLifecycleState(LifecycleState.RESUMED)
                    .build();
        }
        return mReactInstanceManager;
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        getActivity().onBackPressed();
    }

    private boolean mDoRefresh = false;
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mReactInstanceManager != null &&
                mReactInstanceManager.getDevSupportManager().getDevSupportEnabled()) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                mReactInstanceManager.showDevOptionsDialog();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_R && !(getActivity().getCurrentFocus() instanceof EditText)) {
                // Enable double-tap-R-to-reload
                if (mDoRefresh) {
                    mReactInstanceManager.getDevSupportManager().handleReloadJS();
                    mDoRefresh = false;
                } else {
                    mDoRefresh = true;
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    mDoRefresh = false;
                                }
                            },
                            200);
                }
            }
        }
        return false;
    }

}
