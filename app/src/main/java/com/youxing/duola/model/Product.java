package com.youxing.duola.model;

import java.util.List;

/**
 * 产品详情（活动详情）
 *
 * Created by Jun Deng on 15/6/17.
 */
public class Product {

    private Long id;
    private String cover;
    private int joined;
    private double price;
    private String title;
    private String scheduler;
    private String address;
    private String poi;

    private String crowd;
    private List<String> imgs;
    private Customers customers;
    private List<ContentItem> content;


    public long getId() {
        return id;
    }

    public String getCover() {
        return cover;
    }

    public int getJoined() {
        return joined;
    }

    public double getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getScheduler() {
        return scheduler;
    }

    public String getAddress() {
        return address;
    }

    public String getPoi() {
        return poi;
    }

    public String getCrowd() {
        return crowd;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public Customers getCustomers() {
        return customers;
    }

    public List<ContentItem> getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setJoined(int joined) {
        this.joined = joined;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public void setCrowd(String crowd) {
        this.crowd = crowd;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public void setContent(List<ContentItem> content) {
        this.content = content;
    }

    public static class Customers {
        private String text;
        private List<String> avatars;

        public String getText() {
            return text;
        }

        public List<String> getAvatars() {
            return avatars;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setAvatars(List<String> avatars) {
            this.avatars = avatars;
        }
    }

    public static class ContentItem {
        private String title;
        private String style;
        private List<BodyItem> body;

        public String getTitle() {
            return title;
        }

        public String getStyle() {
            return style;
        }

        public List<BodyItem> getBody() {
            return body;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public void setBody(List<BodyItem> body) {
            this.body = body;
        }
    }

    public static class BodyItem {
        private String label;
        private String text;
        private String img;
        private String link;

        public String getLabel() {
            return label;
        }

        public String getText() {
            return text;
        }

        public String getImg() {
            return img;
        }

        public String getLink() {
            return link;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
