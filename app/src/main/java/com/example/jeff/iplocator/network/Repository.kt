package com.example.jeff.iplocator.network

import com.example.jeff.iplocator.model.IpAddress

class Repository {

    var client = RetrofitClientInstance.ipApi


    suspend fun getData(ip: String): IpAddress {
        return client.getIpAddress(ip)

    }

}