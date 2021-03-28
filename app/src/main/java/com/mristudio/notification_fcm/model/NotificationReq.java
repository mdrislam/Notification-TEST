package com.mristudio.notification_fcm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;








public class NotificationReq {

    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("notification")
    @Expose
    private Notification notification;

    @SerializedName("data")
    @Expose
    private Data_ data;

    public Data_ getData() {
        return data;
    }

    public void setData(Data_ data) {
        this.data = data;
    }


    public NotificationReq(String to, Notification notification, Data_ data) {
        this.to = to;
        this.notification = notification;
        this.data = data;
    }

    public NotificationReq(String to, Data_ data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }


    public static class Notification {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("body")
        @Expose
        private String body;

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("click_action")
        @Expose
        private String click_action;

        public Notification(String title, String body, String image, String click_action) {
            this.title = title;
            this.body = body;
            this.image = image;
            this.click_action = click_action;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getClick_action() {
            return click_action;
        }

        public void setClick_action(String click_action) {
            this.click_action = click_action;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

    }






    public static class Data_ {

        @SerializedName("key1")
        @Expose
        private String key1;
        @SerializedName("key2")
        @Expose
        private String key2;

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("body")
        @Expose
        private String body;

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("click_action")
        @Expose
        private String click_action;

        @SerializedName("text")
        @Expose
        private String text;


        public Data_(String key1, String key2) {
            this.key1 = key1;
            this.key2 = key2;
        }

        public Data_(String key1, String key2, String title, String body, String image, String click_action, String text) {
            this.key1 = key1;
            this.key2 = key2;
            this.title = title;
            this.body = body;
            this.image = image;
            this.click_action = click_action;
            this.text = text;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getClick_action() {
            return click_action;
        }

        public void setClick_action(String click_action) {
            this.click_action = click_action;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getKey1() {
            return key1;
        }

        public void setKey1(String key1) {
            this.key1 = key1;
        }

        public String getKey2() {
            return key2;
        }

        public void setKey2(String key2) {
            this.key2 = key2;
        }

    }

}
