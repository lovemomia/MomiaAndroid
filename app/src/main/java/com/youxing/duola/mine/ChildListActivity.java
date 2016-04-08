package com.youxing.duola.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.BasicAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.model.Child;
import com.youxing.common.services.account.AccountChangeListener;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.mine.views.ChildListItem;
import com.youxing.duola.model.AccountModel;
import com.youxing.duola.views.EmptyView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/4/6.
 */
public class ChildListActivity extends SGActivity implements AccountChangeListener, AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private Adapter adapter;

    private boolean select;
    private List<Child> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);

        if (AccountService.instance().isLogin()) {
            childList = AccountService.instance().account().getChildren();
            adapter.notifyDataSetChanged();
        }

        String selectStr = getIntent().getData().getQueryParameter("select");
        if (!TextUtils.isEmpty(selectStr)) {
            select = Boolean.valueOf(selectStr);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        showDialog(ChildListActivity.this, null, "删除该条宝宝信息？", "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestDelChild(childList.get(position));
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return false;
    }

    private void requestDelChild(final Child child) {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("cid", String.valueOf(child.getId())));
        HttpService.post(Constants.domain() + "/user/child/delete", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                AccountService.instance().dispatchAccountChanged(model.getData());
                childList.remove(child);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(ChildListActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (select) {
            Intent data = new Intent();
            data.putExtra("childIndex", position);
            setResult(RESULT_OK, data);
            finish();
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
