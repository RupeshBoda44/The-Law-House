package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SegmentLawListModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("total_pages")
    public String total_pages;
    @SerializedName("segment_of_law_data")
    public List<Segment_of_law_data> segment_of_law_data;

    public static class Segment_of_law_data {
        @SerializedName("created_date")
        public String created_date;
        @SerializedName("content")
        public String content;
        @SerializedName("title")
        public String title;
        @SerializedName("segment_id")
        public String segment_id;
    }
}
