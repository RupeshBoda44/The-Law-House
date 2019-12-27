package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public  class AddSegementLawModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("segment_id")
    public int segment_id;
}
