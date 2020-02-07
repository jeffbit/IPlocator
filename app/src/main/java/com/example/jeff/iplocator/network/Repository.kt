package com.example.jeff.iplocator.network

import com.example.jeff.iplocator.model.IpAddress

class Repository(private val retrofitClient: RetrofitClientInstance) {

    private val client = retrofitClient.ipApi


    suspend fun getData(ip: String): IpAddress {
        return client.getIpAddress(ip)

    }

}