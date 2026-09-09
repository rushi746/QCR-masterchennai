package com.example.newapp.model


import com.google.gson.annotations.SerializedName

data class GetTaskListRequestBody(
    @SerializedName("AssetNo")
    val assetNo: String
)