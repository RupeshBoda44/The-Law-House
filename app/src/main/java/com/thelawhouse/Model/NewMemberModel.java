package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public class NewMemberModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("member_id")
    public int member_id;
}
