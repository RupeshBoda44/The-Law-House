package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public class RegisterModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("otp")
    public String otp;
    @SerializedName("email")
    public String email;
    @SerializedName("user_id")
    public String user_id;
}
