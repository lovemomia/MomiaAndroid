package com.youxing.common.services.http;

import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.volley.PhotoMultipartRequest;
import com.youxing.common.utils.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.youxing.common.app.Constants;
import com.youxing.common.app.Enviroment;
import com.youxing.common.app.YXApplication;
import com.youxing.common.model.BaseModel;
import com.youxing.common.utils.SignTool;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param url 请求url地址
     * @param params 请求参数
     * @param cacheType 缓存类型
     * @param clazz 返回数据model类
     * @param handler 请求回调
     */
    public static void get(String url, List<NameValuePair> params, CacheType cacheType, Class<? extends BaseModel> clazz, final RequestHandler handler) {
        List<NameValuePair> allParams = appendBasicParams(params);
        SignTool.sign(allParams);

        String newUrl = appendForms(url, allParams);
        Log.i("request", "http (GET) " + newUrl);

        Response.Listener<? extends BaseModel> listener = new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel response) {
                if (response.getErrno() == 0) {
                    handler.onRequestFinish(response);

                } else {
                    if (!new ServerErrorHandler().handlerError(response.getErrno(), response.getErrmsg())) {
                        handler.onRequestFailed(response);
                    }
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                BaseModel baseModel = new BaseModel();
                baseModel.setErrno(-1);
                baseModel.setErrmsg(Constants.REQUEST_FAILED_FOR_NET);
                handler.onRequestFailed(baseModel);
            }
        };

        FastJsonRequest request = new FastJsonRequest(Request.Method.GET, newUrl, clazz, getHeaders(), listener, errorListener);
        // 请求加上Tag,用于取消请求
        request.setTag(handler);
        getQueue().add(request);
    }

    /**
     *
     * 发起一个POST请求
     *
     * @param url
     * @param params
     * @param clazz
     * @param handler
     */
    public static void post(String url, List<NameValuePair> params, Class<? extends BaseModel> clazz, final RequestHandler handler) {
        List<NameValuePair> allParams = appendBasicParams(params);
        SignTool.sign(allParams);

        String newUrl = appendForms(url, allParams);
        Log.i("request", "http (POST) " +newUrl);

        Response.Listener<BaseModel> listener = new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel response) {
                if (response.getErrno() == 0) {
                    handler.onRequestFinish(response);

                } else {
                    if (!new ServerErrorHandler().handlerError(response.getErrno(), response.getErrmsg())) {
                        handler.onRequestFailed(response);
                    }
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BaseModel baseModel = new BaseModel();
                baseModel.setErrno(-1);
                baseModel.setErrmsg(Constants.REQUEST_FAILED_FOR_NET);
                handler.onRequestFailed(baseModel);
            }
        };

        FastJsonRequest request = new FastJsonRequest(Request.Method.POST, newUrl, clazz, getHeaders(), listener, errorListener);
        // 请求加上Tag,用于取消请求
        request.setTag(handler);
        getQueue().add(request);
    }

    /**
     * 上传图片
     *
     * @param imageFile
     * @param handler
     */
    public static void uploadImage(File imageFile, final RequestHandler handler) {
        Response.Listener<BaseModel> listener = new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel response) {
                if (response.getErrno() == 0) {
                    handler.onRequestFinish(response);

                } else {
                    handler.onRequestFailed(response);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BaseModel baseModel = new BaseModel();
                baseModel.setErrno(-1);
                baseModel.setErrmsg(Constants.REQUEST_FAILED_FOR_NET);
                handler.onRequestFailed(baseModel);
            }
        };
        PhotoMultipartRequest imageUploadReq = new PhotoMultipartRequest(Constants.domainUploadImage() + "/upload/image", errorListener, listener, imageFile);
        getQueue().add(imageUploadReq);
    }

    /**
     * 根据tag取消请求
     *
     * @param tag
     */
    public static void abort(RequestHandler tag) {
        getQueue().cancelAll(tag);
    }

    private static Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "API1.0(com.youxing.duola;Android)");
        return headers;
    }

    private static List<NameValuePair> appendBasicParams(List<NameValuePair> forms) {
        if (forms == null) {
            forms = new ArrayList<NameValuePair>();
        }

        if (AccountService.instance().isLogin()) {
            forms.add(new BasicNameValuePair("utoken", AccountService.instance().account().getToken()));
        }

        forms.add(new BasicNameValuePair("channel", "android"));
        forms.add(new BasicNameValuePair("city", String.valueOf(CityManager.instance().getChoosedCity().getId())));
        forms.add(new BasicNameValuePair("device", "nexus"));
        forms.add(new BasicNameValuePair("net", "wifi"));
        forms.add(new BasicNameValuePair("os", "4.3"));
        forms.add(new BasicNameValuePair("terminal", "android"));
        forms.add(new BasicNameValuePair("v", Enviroment.versionName()));
        return forms;
    }

    private static String appendForms(String url, List<NameValuePair> forms) {
        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")) {
            sb.append("?");
        }

        if (forms != null) {
            for (NameValuePair from : forms) {
                String value = from.getValue();
                if (value == null || "null".equals(value)) {
                    continue;
                }
                sb.append("&").append(from.getName()).append("=").append(value);
            }
        }

        return sb.toString();
    }

}
