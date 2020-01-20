package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsListModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("total_pages")
    public String total_pages;
    @SerializedName("news_image_url")
    public String news_image_url;
    @SerializedName("news_data")
    public List<News_data> news_data;

    public static class News_data {
        @SerializedName("created_date")
        public String created_date;
        @SerializedName("news_image")
        public String news_image;
        @SerializedName("contents")
        public String contents;
        @SerializedName("title")
        public String title;
        @SerializedName("news_id")
        public String news_id;
    }
}
