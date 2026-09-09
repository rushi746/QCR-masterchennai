package com.example.newapp.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class DeviceStatus(
    /* @SerializedName("CONT_LOC")
     val contLoc: Int = 0,*/
    @SerializedName("Cont_No")
    val contNo: String? = null,
    @SerializedName("Device_Imei")
    val location: String? = null,
    /* @SerializedName("Eqp_No")
     val eqpNo: String? = "",
     @SerializedName("GPS_STATUS")
     val gpsStatus: String? = "",
     @SerializedName("Gps_Time")
     val gpsTime: String? = ""*/
): Parcelable {
    constructor(parcel: Parcel) : this(
//        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
//        p0.writeInt(contLoc)
        p0.writeString(contNo)
        p0.writeString(location)
//        p0.writeString(eqpNo)
//        p0.writeString(gpsStatus)
//        p0.writeString(gpsTime)
    }

    companion object CREATOR : Parcelable.Creator<DeviceStatus> {
        override fun createFromParcel(parcel: Parcel): DeviceStatus {
            return DeviceStatus(parcel)
        }

        override fun newArray(size: Int): Array<DeviceStatus?> {
            return arrayOfNulls(size)
        }
    }
}