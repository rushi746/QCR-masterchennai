package com.example.newapp.model

import com.google.gson.annotations.SerializedName

data class EquipmentStatusResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("total_records") val totalRecords: Int,
    @SerializedName("data") val data: List<EquipmentStatusData>
)

data class EquipmentStatusData(
    @SerializedName("Cont_No") val contNo: String,
    @SerializedName("Device_Imei") val deviceImei: String,
    @SerializedName("Gps_Time") val gpsTime: String,
    @SerializedName("CONT_LOC") val contLoc: String,
    @SerializedName("Eqp_No") val eqpNo: String,
    @SerializedName("GPS_STATUS") val gpsStatus: String
)
