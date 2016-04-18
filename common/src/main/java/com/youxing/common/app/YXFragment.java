package com.youxing.common.app;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class YXFragment extends Fragment {

    // ********* URL Mapping ********** //

    public Intent urlMap(Intent intent) {
        Application app = getActivity().getApplication();
        if (app instanceof YXApplication) {
            return ((YXApplication) app).urlMap(intent);
        } else {
            return intent;
        }
    }

    public void startActivity(String urlSchema) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlSchema)));
    }

    public void startActivity(Intent intent) {
        intent = urlMap(intent);
        if (getActivity() instanceof YXActivity) {
            intent.putExtra("_from", ((YXActivity)getActivity()).getMyUrl());
        }
        intent.putExtra("_startTime", SystemClock.elapsedRealtime());
        super.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        intent = urlMap(intent);
        if (getActivity() instanceof YXActivity) {
            intent.putExtra("_from", ((YXActivity)getActivity()).getMyUrl());
        }
        intent.putExtra("_startTime", SystemClock.elapsedRealtime());
        super.startActivityForResult(intent, requestCode);
    }
}
