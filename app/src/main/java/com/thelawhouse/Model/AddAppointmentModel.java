package com.thelawhouse.Model;

import com.google.gson.annotations.SerializedName;

public class AddAppointmentModel {
    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("appointment_id")
    public int appointment_id;
}
