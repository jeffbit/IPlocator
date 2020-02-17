package com.example.jeff.iplocator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jeff.iplocator.model.IpAddress
import com.example.jeff.iplocator.network.Repository
import com.example.jeff.iplocator.network.Result
import com.example.jeff.iplocator.util.validateIpAddress
import kotlinx.coroutines.launch

class ResultScreenViewModel(private val repository: Repository) : ViewModel() {


    private val _ipAddress = MutableLiveData<Result<IpAddress>>()
    val ipAddress: LiveData<Result<IpAddress>>
        get() = _ipAddress

    private val _lat = MutableLiveData<Double>()
    val lat: LiveData<Double>
        get() = _lat

    private val _lon = MutableLiveData<Double>()
    val lon: LiveData<Double>
        get() = _lon


    fun returnIpAddress(ip: String) {
        viewModelScope.launch {
            try {
                _ipAddress.value = Result.Loading()
                if (validateIpAddress(ip)) {
                    val req = repository.getIp(ip)
                    _ipAddress.value = Result.Success(req)
                    _lon.value = req.longitude
                    _lat.value = req.latitude
                }
            } catch (e: Exception) {
                _ipAddress.value = Result.Error(e)
            }
        }
    }


    fun setIpAddressEmpty() {
        _ipAddress.value = null
    }

    fun clearLatAndLon() {
        _lat.value = null
        _lon.value = null

    }


}



