package com.youxing.common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * 类似
 *
 * Created by Jun Deng on 15/6/4.
 */
public abstract class GroupStyleAdapter extends BasicAdapter {

    private final Context context;

    public GroupStyleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    final public int getCount() {
        int sectionCount = getSectionCount();
        int count = sectionCount;
        for (int i = 0; i < sectionCount; i++) {
            count += getCountInSection(i);
        }
        return count;
    }

    abstract public int getSectionCount();

    abstract public int getCountInSection(int section);

    @Override
    final public View getView(int position, View convertView, ViewGroup parent) {
        int p = 0;
        for (int i = 0; i < getSectionCount(); i++) {
            if (position == p) {
                return getViewForSection(convertView, parent, i);
            }
            p += 1;

            for (int j = 0; j < getCountInSection(i); j++) {
                if (position == p) {
                    View view = getViewForRow(convertView, parent, i, j);
                    view.setBackgroundColor(Color.WHITE);
                    return view;
                }
                p += 1;
            }
        }
        return null;
    }

    abstract public View getViewForRow(View convertView, ViewGroup parent, int section, int row);

    public View getViewForSection(View convertView, ViewGroup parent, int section) {
        View view = new View(context);
        view.setBackgroundColor(Color.parseColor("#F4F4F4"));
        int height = getHeightForSectionView(section);
        if (height >= 0) {
            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, getHeightForSectionView(section)));
        }
        return view;
    }

    /**
     * 设置section高度，-1表示自适应
     *
     * @param section
     * @return
     */
    public int getHeightForSectionView(int section) {
        return 20;
    }

}
