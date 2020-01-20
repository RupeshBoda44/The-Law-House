package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public  class BroadcastModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("broadcast_id")
    public String broadcast_id;
}
