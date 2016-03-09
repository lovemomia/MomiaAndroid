package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 16/3/9.
 */
public class PostOrderModel extends BaseModel {

    private Order data;

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }

}
