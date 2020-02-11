package com.example.jeff.iplocator.viewmodel

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeff.iplocator.R
import com.example.jeff.iplocator.model.IpAddress
import com.example.jeff.iplocator.network.Repository
import com.example.jeff.iplocator.network.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import timber.log.Timber

class SearchScreenViewModel(private val repository: Repository) : ViewModel() {

    lateinit var myMap: GoogleMap
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _ipAddress = MutableLiveData<Result<IpAddress>>()
    val ipAddress: LiveData<Result<IpAddress>>
        get() = _ipAddress

    private val _lat = MutableLiveData<Double>()
    val lat: LiveData<Double>
        get() = _lat

    private val _lon = MutableLiveData<Double>()
    val lon: LiveData<Double>
        get() = _lon


    private val _latLon = MutableLiveData<LatLng>()
    val latLon: LiveData<LatLng>
        get() = _latLon


    fun returnIpAddress(ip: String) {
        uiScope.launch {
            try {
                _ipAddress.value = Result.Loading()
                val req = repository.getIp(ip)
                _ipAddress.value = Result.Success(repository.getIp(ip))

            } catch (e: Exception) {
                _ipAddress.value = Result.Error(e)
            }
        }
    }

    fun setIpAddressEmpty() {
        _ipAddress.value = null
    }

    fun displayMap(lat: Double, lon: Double, location: String? = R.string.isp.toString()) {
        _lat.value = lat
        _lon.value = lon
        val latLon = LatLng(lat, lon)
        Timber.d("Lat: %s, Lon: %s", lat, lon)
        myMap.addMarker(MarkerOptions().position(latLon).title(location))
        myMap.moveCamera(CameraUpdateFactory.newLatLng(latLon))
        myMap.moveCamera(CameraUpdateFactory.zoomTo(10f))


    }

    fun clearLatAndLon() {
        _lat.value = null
        _lon.value = null
    }

    fun clearMap() {
        myMap.clear()
    }


    fun hideTextviewIfNull(
        textView: TextView,
        value: String?
    ) {
        if (value.isNullOrEmpty()) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = value
        }

    }

    fun hideTextviewIfNull(textViewTitle: TextView, textViewData: TextView, value: String?) {
        if (value.isNullOrEmpty()) {
            textViewTitle.visibility = View.GONE
            textViewData.visibility = View.GONE

        } else {
            textViewTitle.visibility = View.VISIBLE
            textViewData.visibility = View.VISIBLE
            textViewData.text = value
        }

    }


    override fun onCleared() {
        super.onCleared()
        Timber.e("Ui scope canceled")
        uiScope.cancel()
    }

//Todo: create error message to display over view if ip is not valid. hide everything in view besides error message to be displayed


}



