package com.youxing.duola.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.services.account.AccountService;
import com.youxing.duola.R;
import com.youxing.duola.app.DLFragment;
import com.youxing.duola.views.TitleBar;

/**
 * Created by Jun Deng on 15/8/3.
 */
public class MineFragment extends DLFragment implements AdapterView.OnItemClickListener {

    private TitleBar titleBar;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mine, null);
        titleBar = (TitleBar) view.findViewById(R.id.titleBar);
        listView = (ListView)view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleBar.getTitleTv().setText("我的");

        titleBar.getRightBtn().setText("登出");
        titleBar.getRightBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountService.instance().dispatchAccountChanged(null);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
