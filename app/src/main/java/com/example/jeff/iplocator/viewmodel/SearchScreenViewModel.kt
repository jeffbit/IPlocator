package com.example.jeff.iplocator.viewmodel

import android.view.View
import android.widget.LinearLayout
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


    fun returnIpAddress(ip: String) {
        uiScope.launch {
            try {
                _ipAddress.value = Result.Loading()
                val req = repository.getIp(ip)
                _ipAddress.value = Result.Success(req)

            } catch (e: Exception) {
                _ipAddress.value = Result.Error(e)
            }
        }
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

    fun setIpAddressEmpty() {
        _ipAddress.value = null
    }

    fun clearLatAndLon() {
        _lat.value = null
        _lon.value = null
    }

    fun clearMap() {
        myMap.clear()
    }

    //should  this be in viewmodel or view?
    fun hideTextIfNull(
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

    //should  this be in viewmodel or view?
    fun hideLinearLayoutIfNull(linearLayout: LinearLayout, textViewData: TextView, value: String?) {
        if (value.isNullOrEmpty()) {
            linearLayout.visibility = View.GONE

        } else {
            linearLayout.visibility = View.VISIBLE
            textViewData.text = value
        }

    }


    override fun onCleared() {
        super.onCleared()
        Timber.e("Ui scope canceled")
        uiScope.cancel()
    }

}



