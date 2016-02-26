package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 16/2/25.
 */
public class BookedCourseListModel extends BaseModel {

    private CourseListModel.CourseList data;

    public CourseListModel.CourseList getData() {
        return data;
    }

    public void setData(CourseListModel.CourseList data) {
        this.data = data;
    }
}
