package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdminListModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("total_pages")
    public String total_pages;
    @SerializedName("users_data")
    public List<Users_data> users_data;

    public static class Users_data {
        @SerializedName("created_date")
        public String created_date;
        @SerializedName("status")
        public String status;
        @SerializedName("otp")
        public String otp;
        @SerializedName("origanal_password")
        public String origanal_password;
        @SerializedName("password")
        public String password;
        @SerializedName("email")
        public String email;
        @SerializedName("mobile_number")
        public String mobile_number;
        @SerializedName("name")
        public String name;
        @SerializedName("user_id")
        public String user_id;
    }
}
