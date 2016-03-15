package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 16/3/15.
 */
public class CouponShareModel extends BaseModel {

    private CouponShareData data;

    public CouponShareData getData() {
        return data;
    }

    public void setData(CouponShareData data) {
        this.data = data;
    }

    public static class CouponShareData {
        private String img;
        private String desc;
        private String cover;
        private String title;
        private String abstracts;
        private String url;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAbstracts() {
            return abstracts;
        }

        public void setAbstracts(String abstracts) {
            this.abstracts = abstracts;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
