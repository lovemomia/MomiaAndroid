package com.youxing.common.app;

import android.app.Application;

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
    }

}
