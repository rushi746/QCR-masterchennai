package com.example.newapp.model


import com.google.gson.annotations.SerializedName

data class Inventory(
    @SerializedName("Cont_No")
    val contNo: String,
    @SerializedName("Last_Loc")
    val lastLoc: String
)