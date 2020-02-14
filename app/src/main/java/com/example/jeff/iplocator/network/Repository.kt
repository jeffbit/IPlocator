package com.example.jeff.iplocator.network

import com.example.jeff.iplocator.model.IpAddress
import timber.log.Timber

class Repository(
    private val retrofitClient: RetrofitClientInstance
) {


    suspend fun getIp(ip: String): IpAddress {
        Timber.e("Get IP: %s", ip)
        return retrofitClient.ipApi.getIpAddress(ip)


    }


}