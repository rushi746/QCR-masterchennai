package com.example.newapp.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("Cont_Loc")
    val contLoc: String,
    @SerializedName("Cont_ref_no")
    val contRefNo: String,
    @SerializedName("cont_size")
    val contSize: String,
    @SerializedName("Container_No")
    val containerNo: String,
    @SerializedName("Job_creation")
    val jobCreation: String,
    @SerializedName("job_id")
    val jobId: Int,
    @SerializedName("task_type")
    val taskType: String,
    @SerializedName("Trailer_no")
    val trailerNo: String
)