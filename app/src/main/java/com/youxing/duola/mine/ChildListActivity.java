package com.youxing.duola.mine;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.youxing.common.adapter.BasicAdapter;
import com.youxing.common.model.Child;
import com.youxing.common.services.account.AccountChangeListener;
import com.youxing.common.services.account.AccountService;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.mine.views.ChildListItem;
import com.youxing.duola.views.EmptyView;

import java.util.List;

/**
 * Created by Jun Deng on 16/4/6.
 */
public class ChildListActivity extends SGActivity implements AccountChangeListener {

    private Adapter adapter;

    private List<Child> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);

        if (AccountService.instance().isLogin()) {
            childList = AccountService.instance().account().getChildren();
            adapter.notifyDataSetChanged();
        }

        AccountService.instance().addListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 0, "添加").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            startActivity("duola://childinfo");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        AccountService.instance().removeListener(this);
        super.onDestroy();
    }

    @Override
    public void onAccountChange(AccountService service) {
        if (service.isLogin()) {
            childList = service.account().getChildren();
            adapter.notifyDataSetChanged();
        }
    }

    class Adapter extends BasicAdapter {

        @Override
        public int getCount() {
            if (childList != null && childList.size() > 0) {
                return childList.size();
            }
            return 1;
        }

        @Override
        public Object getItem(int position) {
            if (childList.size() == 0) {
                return EMPTY;
            }
            return childList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object item = getItem(position);
            if (item == EMPTY) {
                EmptyView cell = EmptyView.create(ChildListActivity.this);
                cell.setMessage("还没有宝宝呢，赶紧添加一个吧~");
                return cell;

            } else {
                ChildListItem cell = ChildListItem.create(ChildListActivity.this);
                cell.setData(childList.get(position));
                return cell;
            }
        }
    }


}
