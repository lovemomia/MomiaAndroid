package com.youxing.duola.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.ReactRootView;
import com.youxing.common.model.Account;
import com.youxing.common.model.City;
import com.youxing.common.services.account.AccountChangeListener;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.utils.CityManager;
import com.youxing.duola.R;
import com.youxing.duola.app.SGFragment;
import com.youxing.duola.home.views.HomeTitleBar;

/**
 * New home page use react-native build.
 *
 * Created by Jun Deng on 16/6/3.
 */
public class HomeFragmentV2 extends SGFragment implements CityManager.CityChangeListener, AccountChangeListener {

    private HomeTitleBar titleBar;
    private ReactRootView mReactRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_home_v2, null);

        titleBar = (HomeTitleBar) rootView.findViewById(R.id.titleBar);
        titleBar.getCityTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("duola://citylist");
            }
        });

        FrameLayout containerLay = (FrameLayout)rootView.findViewById(R.id.container);

        mReactRootView = new ReactRootView(getContext());
        mReactRootView.startReactApplication(getReactInstanceManager("home.bundle", "home/home"), "HomeComponent", null);
        containerLay.addView(mReactRootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CityManager.instance().addListener(this);
        AccountService.instance().addListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupTitle();
    }

    private void setupTitle() {
        titleBar.getAvatarIv().setDefaultImageResId(R.drawable.ic_default_avatar);
        titleBar.getChildLay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("duola://personinfo");
            }
        });
        if (AccountService.instance().isLogin()) {
            Account account = AccountService.instance().account();
            titleBar.getAvatarIv().setImageUrl(account.getAvatar());
            titleBar.getNameTv().setText(account.getNickName() + " " + account.getAgeOfChild());

        } else {
            titleBar.getAvatarIv().setImageUrl("");
            titleBar.getNameTv().setText("松果亲子／点击登录");
        }
    }

    @Override
    public void onCityChanged(City newCity) {
        titleBar.getCityTv().setText(newCity.getName());
    }

    @Override
    public void onAccountChange(AccountService service) {
        setupTitle();
    }

    @Override
    public void onDestroy() {
        CityManager.instance().removeListener(this);
        AccountService.instance().removeListener(this);
        super.onDestroy();
    }

}