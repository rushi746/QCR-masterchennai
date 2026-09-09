package com.example.newapp.api

import com.example.newapp.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface: DICTApiInterface {

    @POST("Equipment/EquipmentStatus/GetAllTabTaskList")
    override suspend fun getTaskList(
        @Body body: GetTaskListRequestBody
    ): Response<List<Task>>

    @POST("Equipment/EquipmentStatus/GetEquipmentStatus")
    override suspend fun getCurrentStatus(
        @Body body: GetTaskListRequestBody
    ): Response<List<DeviceStatus>>


    @POST("Essential/Container/GetAllYardInventoryList")
    override suspend fun getInventory(
        @Query("yard") yard: String
    ): Response<List<Inventory>>

    @POST("")  //  added blank as will be called only for DICT app
    override suspend fun submitInventory(
        containerNo: String,
        location: String,
        asset: String
    ): Response<SubmitInventoryResponse>

    @POST("Essential/Container/ContainerLocationUpdate")
    override suspend fun submitInventory(
        @Body body: SubmitInventoryRequestBody
    ): Response<SubmitInventoryResponse>

    @GET
    override suspend fun getEquipmentStatusMobile(
        @Url url: String,
        @Query("asset_no") assetNo: String
    ): Response<EquipmentStatusResponse>
}