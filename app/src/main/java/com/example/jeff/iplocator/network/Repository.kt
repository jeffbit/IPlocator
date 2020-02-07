package com.example.jeff.iplocator.network

import androidx.lifecycle.LiveData
import com.example.jeff.iplocator.model.IpAddress
import retrofit2.Call

class Repository(private val retrofitClient: RetrofitClientInstance) {

    private val client = retrofitClient.ipApi


    suspend fun getData(ip: String): IpAddress {
        return client.getIpAddress(ip)

    }

}