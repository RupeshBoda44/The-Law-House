package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public class DateCountModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("month_case_count")
    public Month_case_count month_case_count;

    public static class Month_case_count {
        @SerializedName("a31")
        public String a31;
        @SerializedName("a30")
        public String a30;
        @SerializedName("a29")
        public String a29;
        @SerializedName("a28")
        public String a28;
        @SerializedName("a27")
        public String a27;
        @SerializedName("a26")
        public String a26;
        @SerializedName("a25")
        public String a25;
        @SerializedName("a24")
        public String a24;
        @SerializedName("a23")
        public String a23;
        @SerializedName("a22")
        public String a22;
        @SerializedName("a21")
        public String a21;
        @SerializedName("a20")
        public String a20;
        @SerializedName("a19")
        public String a19;
        @SerializedName("a18")
        public String a18;
        @SerializedName("a17")
        public String a17;
        @SerializedName("a16")
        public String a16;
        @SerializedName("a15")
        public String a15;
        @SerializedName("a14")
        public String a14;
        @SerializedName("a13")
        public String a13;
        @SerializedName("a12")
        public String a12;
        @SerializedName("a11")
        public String a11;
        @SerializedName("a10")
        public String a10;
        @SerializedName("a9")
        public String a9;
        @SerializedName("a8")
        public String a8;
        @SerializedName("a7")
        public String a7;
        @SerializedName("a6")
        public String a6;
        @SerializedName("a5")
        public String a5;
        @SerializedName("a4")
        public String a4;
        @SerializedName("a3")
        public String a3;
        @SerializedName("a2")
        public String a2;
        @SerializedName("a1")
        public String a1;
    }
}
