package com.example.newapp.api

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.newapp.listener.FirebaseListener
import com.example.newapp.model.DeviceStatus
import com.example.newapp.model.GetTaskListRequestBody
import com.example.newapp.model.Inventory
import com.example.newapp.model.SubmitInventoryRequestBody
import com.example.newapp.model.SubmitInventoryResponse
import com.example.newapp.model.Task
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

class QCSRRepository {

    companion object {
        val instance: QCSRRepository by lazy { QCSRRepository() }
    }

    fun attachFirebaseListener(assetNo: String, listener: FirebaseListener) {
        val firebaseRef = Firebase.database.getReference("Assets/$assetNo")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot?.let {
                    val status  = it.getValue(DeviceStatus::class.java)
                    status?.let {stat->
                        val res = Resource.Success(mutableListOf(stat))
                        listener.onSuccess(res as Resource<List<DeviceStatus>>)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "ERROR is: $error")
            }
        })
    }

    suspend fun getTaskList(body: GetTaskListRequestBody): Resource<List<Task>> = handleApi { ApiClient.api.getTaskList(body) }

    suspend fun getCurrentStatus(body: GetTaskListRequestBody): Resource<List<DeviceStatus>> {
        return try {
            val response = ApiClient.api.getEquipmentStatusMobile(assetNo = body.assetNo)
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!.data
                val list = data.map {
                    DeviceStatus(contNo = it.contNo, location = it.deviceImei)
                }
                Resource.Success(list)
            } else {
                Resource.Loading()
            }
        } catch (e: Exception) {
            Resource.Loading()
        }
    }

    suspend fun getInventory(yard: String): Resource<List<Inventory>> = handleApi { ApiClient.api.getInventory(yard) }

    suspend fun submitInventory(
        containerNo: String,
        location: String,
        assetNo: String
    ): Resource<SubmitInventoryResponse> = handleApi { ApiClient.api.submitInventory(containerNo, location, assetNo) }

    suspend fun submitInventory(
        requestBody: SubmitInventoryRequestBody
    ): Resource<SubmitInventoryResponse> = handleApi { ApiClient.api.submitInventory(requestBody) }

    private suspend fun<T> handleApi(
        execute: suspend() -> Response<T>
    ): Resource<T>{
        return try {
            val response = execute()
            if (response.body() != null){
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("")
            }
        } catch (e: ConnectException){
            Resource.Error("Network Error. Please check your internet connection!!!")
        } catch (e: SocketTimeoutException){
            Resource.Error("Network timeout. Please check your internet connection!!!")
        } catch (e: Throwable) {
            Resource.Error(e.message?:"Unknown Error")
        }
    }
}

