package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class MyFavListModel extends BaseModel {

    private MyFavLisData data;

    public MyFavLisData getData() {
        return data;
    }

    public void setData(MyFavLisData data) {
        this.data = data;
    }

    public static class MyFavLisData {
        private long totalCount;
        private int nextIndex;
        private List<Product> list;

        public long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

        public int getNextIndex() {
            return nextIndex;
        }

        public void setNextIndex(int nextIndex) {
            this.nextIndex = nextIndex;
        }

        public List<Product> getList() {
            return list;
        }

        public void setList(List<Product> list) {
            this.list = list;
        }
    }
}
