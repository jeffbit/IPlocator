package com.brown.jeff.iplocator.network

import com.brown.jeff.iplocator.model.IpAddress
import retrofit2.http.GET
import retrofit2.http.Path

interface IPAddressAPIService {

    @GET("{ip}")
    suspend fun getIpAddress(@Path("ip") ipAddress: String): IpAddress


}