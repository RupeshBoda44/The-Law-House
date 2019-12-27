package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public class NewCaseModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("case_id")
    public int case_id;
}
