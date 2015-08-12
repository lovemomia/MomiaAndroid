package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/11.
 */
public class FillOrderModel extends BaseModel {

    private FillOrderData data;

    public FillOrderData getData() {
        return data;
    }

    public void setData(FillOrderData data) {
        this.data = data;
    }

    public static class FillOrderData {
        private Contact contacts;
        private List<Sku> skus;

        public Contact getContacts() {
            return contacts;
        }

        public void setContacts(Contact contacts) {
            this.contacts = contacts;
        }

        public List<Sku> getSkus() {
            return skus;
        }

        public void setSkus(List<Sku> skus) {
            this.skus = skus;
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
