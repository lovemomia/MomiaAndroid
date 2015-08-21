package com.youxing.common.app;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.github.mmin18.layoutcast.LayoutCast;
import com.youxing.common.BuildConfig;
import com.youxing.common.services.account.AccountService;

/**
 * Created by Jun Deng on 15/6/3.
 */
public class YXApplication extends Application {

    private static YXApplication instance;

    public static YXApplication instance() {
        if (instance == null) {
            throw new IllegalStateException("Application has not been created");
        }

        return instance;
    }

    static YXApplication _instance() {
        return instance;
    }

    public YXApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            LayoutCast.init(this);
        }
    }

    public Intent urlMap(Intent intent) {
        do {
            Uri uri = intent.getData();
            if (uri == null) {
                break;
            }
            if (uri.getScheme() == null || !"duola".equals(uri.getScheme())) {
                break;
            }

            MappingManager mm = mappingManager();
            if (mm == null) {
                break;
            }

            String host = uri.getHost();
            if (TextUtils.isEmpty(host))
                break;

            host = host.toLowerCase();
            MappingManager.MappingPage page = mm.getPage(host);
            if (page == null) {
                break;
            }

            if (page.needLogin && !AccountService.instance().isLogin()) {
                Intent loginIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("duola://login"));
                loginIntent.putExtra("_destination", uri.toString());
                intent = loginIntent;
            }

        } while (false);

        return intent;
    }

    protected MappingManager mappingManager() {
        return new MappingManager();
    }
}
