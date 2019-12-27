package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public class VerifyOTPModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
}
