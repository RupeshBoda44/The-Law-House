package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CaseListModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("total_pages")
    public String total_pages;
    @SerializedName("case_data")
    public List<Collection_data> collection_data;

    public static class Collection_data {
        @SerializedName("created_by")
        public String created_by;
        @SerializedName("created_date")
        public String created_date;
        @SerializedName("status")
        public String status;
        @SerializedName("email")
        public String email;
        @SerializedName("whatsapp_number")
        public String whatsapp_number;
        @SerializedName("w_country_code")
        public String w_country_code;
        @SerializedName("mobile_no")
        public String mobile_no;
        @SerializedName("stages")
        public String stages;
        @SerializedName("m_country_code")
        public String m_country_code;
        @SerializedName("advocate_party_2")
        public String advocate_party_2;
        @SerializedName("party_name_2")
        public String party_name_2;
        @SerializedName("advocate_party_1")
        public String advocate_party_1;
        @SerializedName("party_name_1")
        public String party_name_1;
        @SerializedName("court")
        public String court;
        @SerializedName("court_room_no")
        public String court_room_no;
        @SerializedName("case_type")
        public String case_type;
        @SerializedName("case_next_date")
        public String case_next_date;
        @SerializedName("case_last_date")
        public String case_last_date;
        @SerializedName("case_id")
        public String case_id;
    }
}
