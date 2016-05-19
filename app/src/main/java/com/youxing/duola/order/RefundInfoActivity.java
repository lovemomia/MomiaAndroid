package com.youxing.duola.order;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.model.Order;
import com.youxing.duola.order.views.RefundHeaderItem;
import com.youxing.duola.order.views.RefundStatusItem;
import com.youxing.duola.views.SimpleListItem;

/**
 * Created by Jun Deng on 16/5/5.
 */
public class RefundInfoActivity extends SGActivity {

    private Order order;
    private boolean back2root;

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        order = getIntent().getParcelableExtra("order");
        back2root = getIntent().getBooleanExtra("back2root", false);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter(this);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (back2root) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("duola://myorderlist"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (back2root) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("duola://myorderlist"));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            return 2;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 1;
            }
            return 2;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            if (section == 0) {
                RefundHeaderItem cell = RefundHeaderItem.create(RefundInfoActivity.this);
                cell.setData(order);
                return cell;
            } else {
                if (row == 0) {
                    SimpleListItem cell = SimpleListItem.create(RefundInfoActivity.this);
                    cell.setTitle("退款流程");
                    return cell;

                } else {
                    RefundStatusItem cell = RefundStatusItem.create(RefundInfoActivity.this);
                    cell.setData(order);
                    return cell;
                }
            }
        }
    }
}
