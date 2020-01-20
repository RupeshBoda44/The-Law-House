package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class NotificationModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("total_pages")
    public int total_pages;
    @SerializedName("notification_data")
    public List<Notification_data> notification_data;

    public static class Notification_data {
        @SerializedName("created_date")
        public String created_date;
        @SerializedName("created_by")
        public String created_by;
        @SerializedName("user_type")
        public String user_type;
        @SerializedName("notification_read")
        public String notification_read;
        @SerializedName("notification_content")
        public String notification_content;
        @SerializedName("notification_user_id")
        public String notification_user_id;
        @SerializedName("notification_id")
        public String notification_id;
        @SerializedName("flag")
        private boolean flag;

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getNotification_read() {
            return notification_read;
        }

        public void setNotification_read(String notification_read) {
            this.notification_read = notification_read;
        }

        public String getNotification_content() {
            return notification_content;
        }

        public void setNotification_content(String notification_content) {
            this.notification_content = notification_content;
        }

        public String getNotification_user_id() {
            return notification_user_id;
        }

        public void setNotification_user_id(String notification_user_id) {
            this.notification_user_id = notification_user_id;
        }

        public String getNotification_id() {
            return notification_id;
        }

        public void setNotification_id(String notification_id) {
            this.notification_id = notification_id;
        }

        public boolean getFlag() {
            return flag;
        }


        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}
