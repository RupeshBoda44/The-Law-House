package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BrodcastDetailModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("broadcast_data")
    public List<Broadcast_data> broadcast_data;

    public static class Broadcast_data {
        @SerializedName("created_date")
        public String created_date;
        @SerializedName("broadcast_message")
        public String broadcast_message;
        @SerializedName("broadcast_id")
        public String broadcast_id;
    }
}
