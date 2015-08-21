package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 15/8/18.
 */
public class PostOrderModel extends BaseModel {

    private PostOrderData data;

    public PostOrderData getData() {
        return data;
    }

    public void setData(PostOrderData data) {
        this.data = data;
    }

    public static class PostOrderData {

        private int count;
        private long id;
        private long productId;
        private long skuId;
        private String participants;
        private double totalFee;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public long getSkuId() {
            return skuId;
        }

        public void setSkuId(long skuId) {
            this.skuId = skuId;
        }

        public String getParticipants() {
            return participants;
        }

        public void setParticipants(String participants) {
            this.participants = participants;
        }

        public double getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(double totalFee) {
            this.totalFee = totalFee;
        }
    }
}
