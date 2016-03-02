package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class OrderDetailModel extends BaseModel {

    private Order data;

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }

}
