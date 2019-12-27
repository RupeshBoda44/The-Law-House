package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class UsefullLinkListModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("total_pages")
    public String total_pages;
    @SerializedName("use_full_link_data")
    public List<Use_full_link_data> use_full_link_data;

    public static class Use_full_link_data {
        @SerializedName("created_date")
        public String created_date;
        @SerializedName("title")
        public String status;
        @SerializedName("use_link")
        public String content;
        @SerializedName("use_full_link_id")
        public String use_full_link_id;
    }
}
