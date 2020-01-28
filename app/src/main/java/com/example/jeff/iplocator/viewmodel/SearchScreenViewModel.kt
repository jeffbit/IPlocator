package com.example.jeff.iplocator.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeff.iplocator.model.IpAddress
import com.example.jeff.iplocator.network.Repository
import kotlinx.coroutines.*

class SearchScreenViewModel() : ViewModel() {
    private val TAG: String = "SEARCH_SCREEN_VIEWMODEL"

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    //convert to dependency injection for cleaner code and better testing
    val repository: Repository =
        Repository()


    private val _ipAddress = MutableLiveData<IpAddress>()
    val ipAddress: LiveData<IpAddress>
        get() = _ipAddress

    private val _loadingScreen = MutableLiveData<Boolean>()
    val loadingScreen: LiveData<Boolean>
        get() = _loadingScreen

    private val _showErrorMessage = MutableLiveData<Boolean>()
    val showErrorMessage: LiveData<Boolean>
        get() = _showErrorMessage

    private val _showResults = MutableLiveData<Boolean>()
    val showResults: LiveData<Boolean>
        get() = _showResults


    fun returnIpAddress(ip: String) {
        _loadingScreen.value = false
        _showErrorMessage.value = false
        _showResults.value = false
        uiScope.launch {
            try {
                _ipAddress.value = repository.getData(ip)
                _loadingScreen.value = true
                _showResults.value = true
            } catch (e: java.lang.Exception) {
                showLogError(e.localizedMessage)
                _ipAddress.value = null
                _showErrorMessage.value = true
                _showResults.value = false
                _loadingScreen.value = true

            }

        }
    }


//    val testIp = liveData {
//        Log.e("testIp", "Started")
//        try {
//            val test = repository.getData("1.1.1.1")
//            emit(test)
//            Log.e("Success", "Submited")
//        } catch (e: Exception) {
//            showLogError(e.localizedMessage!!)
//        }
//    }


    private fun showLogError(message: String) {
        Log.e(TAG, message)

    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "UI Scope canceled")
        uiScope.cancel()
    }
}