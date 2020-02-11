package com.example.jeff.iplocator.network

import com.example.jeff.iplocator.model.IpAddress
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IPAddressAPIService {

    @GET("{ip}")
    suspend fun getIpAddress(@Path("ip") ipAddress: String): IpAddress

}