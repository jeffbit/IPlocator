package com.example.jeff.iplocator.network

import com.example.jeff.iplocator.model.IpAddress
import retrofit2.http.GET
import retrofit2.http.Path

interface IPAddressAPIService {

    //new Retrofit coroutine support allows us to return IPAddress without Deferred or Call in a suspend function

    @GET("{ip}")
    suspend fun getIpAddress(@Path("ip") ipAddress: String): IpAddress


}