package com.example.newapp.model


import com.google.gson.annotations.SerializedName

data class SubmitInventoryRequestBody(
    @SerializedName("Cont_No")
    val contNo: String,
    @SerializedName("Location")
    val location: String,
    @SerializedName("asset_id")
    val assetId: String

)