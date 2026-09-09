package com.example.newapp.ui.taskList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newapp.api.QCSRRepository
import com.example.newapp.api.Resource
import com.example.newapp.model.GetTaskListRequestBody
import com.example.newapp.model.Task
import com.example.newapp.utils.PreferenceManager
import kotlinx.coroutines.launch

class TaskListViewModel(
    val app: Application
): AndroidViewModel(app) {

    private val repo by lazy { QCSRRepository.instance }

    private val assetNo = PreferenceManager.getInstance(app)
        .getValue(PreferenceManager.KEY_ASSET_NO)

    private val taskList = MutableLiveData<Resource<List<Task>>>()

    fun getTaskList():LiveData<Resource<List<Task>>> = taskList

    fun fetchTaskList() = viewModelScope.launch {
        var requestBody = GetTaskListRequestBody(assetNo!!)
        val response = repo.getTaskList(requestBody)
        taskList.postValue(response)
    }
}