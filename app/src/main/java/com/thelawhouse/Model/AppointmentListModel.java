package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppointmentListModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("total_pages")
    public String total_pages;
    @SerializedName("appointment_data")
    public List<Appointment_data> appointment_data;

    public static class Appointment_data {
        @SerializedName("created_by")
        public String created_by;
        @SerializedName("created_date")
        public String created_date;
        @SerializedName("appointment_status")
        public String appointment_status;
        @SerializedName("appointment_time")
        public String appointment_time;
        @SerializedName("appointment_date")
        public String appointment_date;
        @SerializedName("appointment_reason")
        public String appointment_reason;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("c_code")
        public String c_code;
        @SerializedName("email")
        public String email;
        @SerializedName("name")
        public String name;
        @SerializedName("appointment_id")
        public String appointment_id;
    }
}
