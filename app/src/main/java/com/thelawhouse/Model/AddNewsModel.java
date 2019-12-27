package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public  class AddNewsModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("news_id")
    public int news_id;
}
