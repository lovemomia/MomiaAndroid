package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/3/8.
 */
public class SkuListModel extends BaseModel {

    private SkuListData data;

    public SkuListData getData() {
        return data;
    }

    public void setData(SkuListData data) {
        this.data = data;
    }

    public static class SkuListData {

        private List<Sku> skus;
        private List<Sku> packages;
        private Contact contact;

        public List<Sku> getSkus() {
            return skus;
        }

        public void setSkus(List<Sku> skus) {
            this.skus = skus;
        }

        public List<Sku> getPackages() {
            return packages;
        }

        public void setPackages(List<Sku> packages) {
            this.packages = packages;
        }

        public Contact getContact() {
            return contact;
        }

        public void setContact(Contact contact) {
            this.contact = contact;
        }
    }

    public static class Contact {

        private String mobile;
        private String name;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
