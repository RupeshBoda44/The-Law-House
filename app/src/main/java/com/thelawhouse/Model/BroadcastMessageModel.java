package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public class BroadcastMessageModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("broadcast_data")
    public String broadcast_data;
}
