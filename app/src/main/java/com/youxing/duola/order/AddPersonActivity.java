package com.youxing.duola.order;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import com.youxing.common.adapter.BasicAdapter;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.views.InputListItem;
import com.youxing.duola.views.SelectListItem;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 新增出行人
 *
 * Created by Jun Deng on 15/6/9.
 */
public class AddPersonActivity extends SGActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        Adapter adapter = new Adapter();
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        findViewById(R.id.done).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.done) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1) {
            final String[] names = { "男", "女"};
            AlertDialog dialog = new AlertDialog.Builder(AddPersonActivity.this).setItems(names, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create();
            dialog.show();


        } else if (position == 2) {
            Calendar d = Calendar.getInstance(Locale.CHINA);
            Date myDate=new Date();
            d.setTime(myDate);
            int year=d.get(Calendar.YEAR);
            int month=d.get(Calendar.MONTH);
            int day=d.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dlg=new DatePickerDialog(AddPersonActivity.this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                }

            }, year, month, day);
            dlg.show();
        }
    }

    private void chooseSexy() {

    }

    class Adapter extends BasicAdapter {

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (position == 0) {
                view = InputListItem.create(AddPersonActivity.this);
            } else {
                view = SelectListItem.create(AddPersonActivity.this);
            }
            return view;
        }


    }
}
