package com.youxing.duola.react;

import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.youxing.common.app.Enviroment;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.utils.CityManager;
import com.youxing.common.utils.Log;
import com.youxing.common.utils.SignTool;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/5/31.
 */
public class RNCommon extends ReactContextBaseJavaModule {

    public RNCommon(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNCommon";
    }

    @ReactMethod
    public void openUrl(String url) {
        getCurrentActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://" + url)));
    }

    @ReactMethod
    public void wrapParams(ReadableMap params, Callback callback) {
        List<NameValuePair> forms = new ArrayList<NameValuePair>();

        if (AccountService.instance().isLogin()) {
            forms.add(new BasicNameValuePair("utoken", AccountService.instance().account().getToken()));
        }

        forms.add(new BasicNameValuePair("channel", Enviroment.channel()));
        forms.add(new BasicNameValuePair("city", String.valueOf(CityManager.instance().getChoosedCity().getId())));
        forms.add(new BasicNameValuePair("device", Enviroment.deviceType()));
        forms.add(new BasicNameValuePair("net", Enviroment.getNetworkType()));
        forms.add(new BasicNameValuePair("os", Enviroment.osInfo()));
        forms.add(new BasicNameValuePair("terminal", "android"));
        forms.add(new BasicNameValuePair("v", Enviroment.versionName()));

        if (params != null) {
            ReadableMapKeySetIterator iterator = params.keySetIterator();
            while (iterator.hasNextKey()) {
                String key = iterator.nextKey();
                String value;

                ReadableType type = params.getType(key);
                if (type == ReadableType.Boolean) {
                    value = String.valueOf(params.getBoolean(key));
                } else if (type == ReadableType.Number) {
                    value = String.valueOf(params.getInt(key));
                } else if (type == ReadableType.String) {
                    value = params.getString(key);
                } else {
                    throw new IllegalArgumentException("value type is not supported!!!");
                }

                forms.add(new BasicNameValuePair(key, value));
            }
        }

        SignTool.sign(forms);

        WritableMap map = Arguments.createMap();
        for (NameValuePair pair : forms) {
            map.putString(pair.getName(), pair.getValue());
        }

        callback.invoke(null, map);
    }

    @ReactMethod
    public void wrapUrl(String url, Callback callback) {
        Log.d("wrapUrl:");
        callback.invoke(null, "newurl");
    }

}
