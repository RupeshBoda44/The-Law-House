package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AboutUsModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("news_image_url")
    public String news_image_url;
    @SerializedName("news_data")
    public List<News_data> news_data;

    public static class News_data {
        @SerializedName("created_date")
        public String created_date;
        @SerializedName("content")
        public String content;
        @SerializedName("title")
        public String title;
        @SerializedName("about_id")
        public String about_id;
    }
}
