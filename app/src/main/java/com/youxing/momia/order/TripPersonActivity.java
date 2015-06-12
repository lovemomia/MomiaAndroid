package com.youxing.momia.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.momia.R;
import com.youxing.momia.app.TQActivity;
import com.youxing.momia.model.Person;
import com.youxing.momia.order.views.TripPersonItem;
import com.youxing.momia.views.SectionView;

import java.util.ArrayList;
import java.util.List;

/**
 * 出行人选择
 *
 * Created by Jun Deng on 15/6/9.
 */
public class TripPersonActivity extends TQActivity implements AdapterView.OnItemClickListener {

    private Adapter adapter;

    private List<Person> personList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_person);

        ListView listView = (ListView)findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        requestData();
    }

    private void requestData() {
        personList.add(new Person());
        personList.add(new Person());
        personList.add(new Person());

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItemCompat.setShowAsAction(menu.add("添加"),
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ("添加".equals(item.getTitle())) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tq://addperson")));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (view instanceof TripPersonItem) {
            Person person = personList.get(indexPath.row);
            if (person.selected) {
                person.selected = false;

            } else {
                person.selected = true;
            }
            ((TripPersonItem)view).setChecked(person.selected);
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(TripPersonActivity.this);
        }

        @Override
        public int getSectionCount() {
            return 1;
        }

        @Override
        public int getCountInSection(int section) {
            return personList.size();
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            SectionView view = SectionView.create(TripPersonActivity.this);
            view.setTitle("请选择成人1名，儿童1名");
            return view;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = convertView;
            if (!(view instanceof TripPersonItem)) {
                view = LayoutInflater.from(TripPersonActivity.this).inflate(R.layout.layout_trip_person_item, null);
            }
            return view;
        }
    }
}
