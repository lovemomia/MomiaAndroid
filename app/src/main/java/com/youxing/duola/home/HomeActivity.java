package com.youxing.duola.home;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.duola.R;
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.home.views.HomeHeaderView;
import com.youxing.duola.home.views.HomeListItem;

/**
 * Created by Jun Deng on 15/6/8.
 */
public class HomeActivity extends DLActivity {

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.addHeaderView(createHeaderView());
        adapter = new Adapter();
        listView.setAdapter(adapter);

        setTitle("上海");

    }

    private HomeHeaderView createHeaderView() {
        HomeHeaderView header = HomeHeaderView.create(this);
        header.setData();
        return header;
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(HomeActivity.this);
        }

        @Override
        public int getSectionCount() {
            return 3;
        }

        @Override
        public int getCountInSection(int section) {
            return 1;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = convertView;
            if (!(view instanceof HomeListItem)) {
                view = HomeListItem.create(HomeActivity.this);
            }
            ((HomeListItem) view).setData();
            return view;
        }

        @Override
        public int getHeightForSectionView(int section) {
            return 15;
        }
    }
}
