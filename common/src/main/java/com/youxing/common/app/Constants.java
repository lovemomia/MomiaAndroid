package com.youxing.common.app;

/**
 * 基本常量定义
 *
 * Created by Jun Deng on 15/6/3.
 */
public class Constants {

    public static final boolean DEBUG = true;

    public static final String REQUEST_FAILED_FOR_NET = "网络异常，请稍后再试";

    public static final String WECHAT_APP_ID = "wx50b2ac03c88ad6e7";

    public static final String DOMAIN_ONLINE = "http://i.sogokids.com";
    public static final String DOMAIN_QA = "http://i.momia.cn";

    public static final String DOMAIN_ONLINE_HTTPS = "https://i.sogokids.com";
    public static final String DOMAIN_QA_HTTPS = "https://i.momia.cn";

    public static final String DOMAIN_ONLINE_IMAGE = "http://s.sogokids.com";
    public static final String DOMAIN_QA_IMAGE = "http://s.momia.cn";

    public static final String DOMAIN_ONLINE_WEB = "http://m.sogokids.com";
    public static final String DOMAIN_QA_WEB = "http://m.momia.cn";

    public static String domain() {
        if (DEBUG) {
            return DOMAIN_QA;
        }
        return DOMAIN_ONLINE;
    }

    public static String domainHttps() {
        if (DEBUG) {
            return DOMAIN_QA_HTTPS;
        }
        return DOMAIN_ONLINE_HTTPS;
    }

    public static String domainImage() {
        if (DEBUG) {
            return DOMAIN_QA_IMAGE;
        }
        return DOMAIN_ONLINE_IMAGE;
    }

    public static String domainWeb() {
        if (DEBUG) {
            return DOMAIN_QA_WEB;
        }
        return DOMAIN_ONLINE_WEB;
    }
}
