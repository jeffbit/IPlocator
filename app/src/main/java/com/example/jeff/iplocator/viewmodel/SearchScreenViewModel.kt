package com.example.jeff.iplocator.viewmodel

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeff.iplocator.R
import com.example.jeff.iplocator.model.IpAddress
import com.example.jeff.iplocator.network.Repository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import timber.log.Timber

class SearchScreenViewModel(private val repository: Repository) : ViewModel() {


    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _ipAddress = MutableLiveData<IpAddress>()
    val ipAddress: LiveData<IpAddress>
        get() = _ipAddress

    //Loading and Error handling
    private val _loadingScreen = MutableLiveData<Boolean>()
    val loadingScreen: LiveData<Boolean>
        get() = _loadingScreen

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage
    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error


    private val _lat = MutableLiveData<Double>()
    val lat: LiveData<Double>
        get() = _lat

    private val _lon = MutableLiveData<Double>()
    val lon: LiveData<Double>
        get() = _lon


    private val _latLon = MutableLiveData<LatLng>()
    val latLon: LiveData<LatLng>
        get() = _latLon
    lateinit var myMap: GoogleMap

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

    fun stopLoading() {
        _loadingScreen.value = false
    }


    fun returnIpAddress(ip: String) {
        _loadingScreen.value = false
        _error.value = false

        uiScope.launch {
            try {
                _ipAddress.value = repository.getIp(ip)
                _loadingScreen.value = true

            } catch (e: Exception) {
                Timber.e(e.message)
                _errorMessage.value = e.message
                _ipAddress.value = null
                _error.value = true

                _loadingScreen.value = false

            }
        }
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



