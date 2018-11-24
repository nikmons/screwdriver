package com.mypackage.project;

import com.google.gson.annotations.SerializedName;

public class DevicesToRepairModel {
    long serviceId;
    String device;
    String problem;
    String endDate;
    int nextToAction;
    @SerializedName("title")
    String trackingNumber;
    String name_surname;
    String address;
    int deliveredToHome;
}
