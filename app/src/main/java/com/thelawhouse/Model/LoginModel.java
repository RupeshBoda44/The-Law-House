package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("admin_access")
    public String admin_access;
    @SerializedName("user_data")
    public User user;

    public static class User {
        @SerializedName("created_date")
        public String created_date;
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
