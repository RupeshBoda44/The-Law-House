package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public class AddUsefullLinkModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("use_full_link_id")
    public int use_full_link_id;
}
