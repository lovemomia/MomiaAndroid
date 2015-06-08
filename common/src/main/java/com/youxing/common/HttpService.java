package com.youxing.common;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.youxing.common.app.Constants;
import com.youxing.common.app.Enviroment;
import com.youxing.common.app.YXApplication;
import com.youxing.common.model.NetModel;
import com.youxing.common.utils.SignTool;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * http数据请求服务
 *
 * Created by Jun Deng on 15/6/2.
 */
public class HttpService {

    private volatile static RequestQueue requestQueue;

    public static RequestQueue getQueue() {
        if (requestQueue == null) {
            synchronized (HttpService.class) {
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(YXApplication.instance());
                }
            }
        }
        return requestQueue;
    }

    /**
     *
     * 发起一个GET请求
     *
     * @param tag 请求的tag，用于取消请求的时候区分，可以多个请求同一个tag
     * @param url 请求url地址
     * @param params 请求参数
     * @param cacheType 缓存类型
     * @param clazz 返回数据model类
     * @param handler 请求回调
     */
    public static void get(Object tag, String url,
                            List<NameValuePair> params, CacheType cacheType, Class<NetModel> clazz, final RequestHandler handler) {
        SignTool.sign(appendBasicParams(params));

        String newUrl = appendForms(url, params);
        Log.i("request", "http (GET) " + newUrl);

        Response.Listener<NetModel> listener = new Response.Listener<NetModel>() {
            @Override
            public void onResponse(NetModel response) {
                if (response.getErrno() == 0) {
                    handler.onRequestFinish(response);

                } else {
                    handler.onRequestFailed(response);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.onRequestFailed(new NetModel(-1, Constants.REQUEST_FAILED_FOR_NET));
            }
        };
        FastJsonRequest request = new FastJsonRequest(Request.Method.GET, url, clazz, null, listener, errorListener);
        // 请求加上Tag,用于取消请求
        request.setTag(tag);
        getQueue().add(request);
    }

    public static void post(Object tag, String url,
                            List<NameValuePair> params, Class<NetModel> clazz, final RequestHandler handler) {
        SignTool.sign(appendBasicParams(params));

        String newUrl = appendForms(url, params);
        Log.i("request", "http (POST) " +newUrl);

        Response.Listener<NetModel> listener = new Response.Listener<NetModel>() {
            @Override
            public void onResponse(NetModel response) {
                if (response.getErrno() == 0) {
                    handler.onRequestFinish(response);

                } else {
                    handler.onRequestFailed(response);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.onRequestFailed(new NetModel(-1, Constants.REQUEST_FAILED_FOR_NET));
            }
        };

        FastJsonRequest request = new FastJsonRequest(Request.Method.POST, url, clazz, null, listener, errorListener);
        // 请求加上Tag,用于取消请求
        request.setTag(tag);
        getQueue().add(request);
    }

    /**
     * 根据tag取消请求
     *
     * @param tag
     */
    public static void abort(Object tag) {
        getQueue().cancelAll(tag);
    }

    private static List<NameValuePair> appendBasicParams(List<NameValuePair> forms) {
        if (forms == null) {
            forms = new ArrayList<NameValuePair>();
        }

        forms.add(new BasicNameValuePair("timestamp", String.valueOf(System
                .currentTimeMillis())));
        forms.add(new BasicNameValuePair("channel", "android"));
        forms.add(new BasicNameValuePair("v", Enviroment.versionName()));
        return forms;
    }

    private static String appendForms(String url, List<NameValuePair> forms) {
        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")) {
            sb.append("?");
        }

        for (NameValuePair from : forms) {
            String value = from.getValue();
            if (value == null || "null".equals(value)) {
                continue;
            }
            sb.append("&").append(from.getName()).append("=").append(value);
        }

        return sb.toString();
    }

}
