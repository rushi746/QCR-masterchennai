package com.example.newapp.ui.mapContainer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newapp.BuildConfig
import com.example.newapp.api.QCSRRepository
import com.example.newapp.api.Resource
import com.example.newapp.model.Inventory
import com.example.newapp.model.SubmitInventoryRequestBody
import com.example.newapp.model.SubmitInventoryResponse
import com.example.newapp.utils.PreferenceManager
import kotlinx.coroutines.launch

class MapContainerViewModel(
    val app: Application
): AndroidViewModel(app) {

    private val repo by lazy { QCSRRepository.instance }

    private val assetNo = PreferenceManager.getInstance(app)
        .getValue(PreferenceManager.KEY_ASSET_NO)

    private val inventoryList = MutableLiveData<Resource<List<Inventory>>>()

    private val submitInventoryResponse = MutableLiveData<Resource<SubmitInventoryResponse>>()

    fun getInventoryList():LiveData<Resource<List<Inventory>>> = inventoryList

    fun getSubmitInventoryResponse():LiveData<Resource<SubmitInventoryResponse>> = submitInventoryResponse

    fun fetchInventory() = viewModelScope.launch {
        val response = repo.getInventory("")
        inventoryList.postValue(response)
    }

    fun submitInventory(containerNo: String, location: String) = viewModelScope.launch {
        if (!containerNo.isNullOrBlank() && !location.isNullOrBlank() && !assetNo.isNullOrBlank()) {
            val response =  if(BuildConfig.FLAVOR.equals("dict", true)) {
                repo.submitInventory(containerNo.uppercase(), location, assetNo)
            } else {
                val requestBody = SubmitInventoryRequestBody(containerNo.uppercase(), location, assetNo)
                repo.submitInventory(requestBody)
            }
            submitInventoryResponse.postValue(response)
        }
    }
}