package com.example.newapp.model

import com.google.gson.annotations.SerializedName

data class SubmitInventoryResponse(
    @SerializedName("ErrorMsg")
    val errorMsg: String,
    @SerializedName("Status")
    val status: String,
)
