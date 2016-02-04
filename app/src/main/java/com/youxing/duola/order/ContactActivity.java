package com.youxing.duola.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.views.SimpleListItem;

/**
 * Created by Jun Deng on 15/8/28.
 */
public class ContactActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private static final int MENU_SUBMIT = 1;

    private Adapter adapter;

    private String name;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = (ListView)findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        name = getIntent().getData().getQueryParameter("name");
        phone = getIntent().getData().getQueryParameter("phone");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_SUBMIT, 0, "完成").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_SUBMIT) {
            if (TextUtils.isEmpty(name)) {
                showDialog(ContactActivity.this, "姓名不能为空");
                return true;
            }
            if (TextUtils.isEmpty(phone)) {
                showDialog(ContactActivity.this, "电话号不能为空");
                return true;
            }
            Intent data = new Intent();
            data.putExtra("name", name);
            data.putExtra("phone", phone);
            setResult(RESULT_OK, data);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.row == 0) {
            showInputDialog("请输入姓名", new OnInputDoneListener() {
                @Override
                public void onInputDone(String text) {
                    name = text;
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            showInputDialog("请输入手机号", new OnInputDoneListener() {
                @Override
                public void onInputDone(String text) {
                    phone = text;
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void showInputDialog(String title, final OnInputDoneListener listener) {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this).setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(input).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onInputDone(input.getText().toString());
            }
        }).setNegativeButton("取消", null).show();
    }

    interface OnInputDoneListener {
        void onInputDone(String text);
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(ContactActivity.this);
        }

        @Override
        public int getSectionCount() {
            return 1;
        }

        @Override
        public int getCountInSection(int section) {
            return 2;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            SimpleListItem item = SimpleListItem.create(ContactActivity.this);
            item.setShowArrow(true);
            if (row == 0) {
                item.setTitle("姓名");
                if (name != null) {
                    item.setSubTitle(name);
                }
            } else {
                item.setTitle("手机号");
                if (phone != null) {
                    item.setSubTitle(phone);
                }
            }
            return item;
        }
    }
}
