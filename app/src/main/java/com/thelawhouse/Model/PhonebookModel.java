package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhonebookModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("total_pages")
    public String total_pages;
    @SerializedName("phone_book_data")
    public List<Phone_book_data> phone_book_data;

    public static class Phone_book_data {
        @SerializedName("mobile_number")
        public String mobile_number;
        @SerializedName("email")
        public String email;
        @SerializedName("name")
        public String name;
        @SerializedName("member_id")
        public String member_id;
    }
}
