package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 16/3/4.
 */
public class CourseDetailModel extends BaseModel {

    private Course data;

    public Course getData() {
        return data;
    }

    public void setData(Course data) {
        this.data = data;
    }
}
