package com.example.jeff.iplocator.network

import com.example.jeff.iplocator.model.IpAddress
import timber.log.Timber

class Repository(private val retrofitClient: RetrofitClientInstance) {

    private val client = retrofitClient.ipApi

    suspend fun getIp(ip: String): IpAddress {
        Timber.e("Get Ip")
        return client.getIpAddress(ip)

    }


}