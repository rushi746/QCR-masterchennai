package com.example.newapp.ui.currentStatusReport

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newapp.api.QCSRRepository
import com.example.newapp.api.Resource
import com.example.newapp.listener.FirebaseListener
import com.example.newapp.model.DeviceStatus
import com.example.newapp.model.GetTaskListRequestBody
import com.example.newapp.utils.PreferenceManager
import kotlinx.coroutines.launch

class CurrentStatusViewModel(
    val app: Application
): AndroidViewModel(app) {

    private val repo by lazy { QCSRRepository.instance }

    private val assetNo = PreferenceManager.getInstance(app)
        .getValue(PreferenceManager.KEY_ASSET_NO)
    
    init {
        attachFirebase()
    }

    private fun attachFirebase() = viewModelScope.launch {
        repo.attachFirebaseListener(assetNo!!, object : FirebaseListener {
            override fun onSuccess(deviceStatus: Resource<List<DeviceStatus>>) {
                deviceStatusList.postValue(deviceStatus)
            }
        })
    }

    private val deviceStatusList = MutableLiveData<Resource<List<DeviceStatus>>>()

    fun getDeviceList():LiveData<Resource<List<DeviceStatus>>> = deviceStatusList

    fun fetchStatus() = viewModelScope.launch {
        var requestBody = GetTaskListRequestBody(assetNo!!)
        val response = repo.getCurrentStatus(requestBody)
        deviceStatusList.postValue(response)
    }
}