package com.example.newapp.listener

import com.example.newapp.api.Resource
import com.example.newapp.model.DeviceStatus

interface FirebaseListener {
    fun onSuccess(deviceStatus: Resource<List<DeviceStatus>>)
}