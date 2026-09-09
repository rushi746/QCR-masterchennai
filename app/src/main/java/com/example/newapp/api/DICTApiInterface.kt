package com.example.newapp.api

import com.example.newapp.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Url

interface DICTApiInterface {

    @POST("Task/TaskList")
    suspend fun getTaskList(
        @Body body: GetTaskListRequestBody
    ): Response<List<Task>>

    @POST("Task/CurrentStatus")
    suspend fun getCurrentStatus(
        @Body body: GetTaskListRequestBody
    ): Response<List<DeviceStatus>>


    @POST("Inventory/YARD_INVENTORY")
    suspend fun getInventory(
        @Query("yard") yard: String
    ): Response<List<Inventory>>

    @POST("Inventory/InvetoryUpdate")
    suspend fun submitInventory(
        @Query("contno") containerNo: String,
        @Query("loc") location: String,
        @Query("asset") asset: String
    ): Response<SubmitInventoryResponse>

    @POST("") // added blank as only called for non DICT app
    suspend fun submitInventory(
        @Body body: SubmitInventoryRequestBody
    ): Response<SubmitInventoryResponse>

    @GET
    suspend fun getEquipmentStatusMobile(
        @Url url: String = "http://103.186.173.76:5000/v1/reports/equipment-status-mobile",
        @Query("asset_no") assetNo: String
    ): Response<EquipmentStatusResponse>
}