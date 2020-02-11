package com.example.jeff.iplocator.network

import androidx.lifecycle.LiveData
import com.example.jeff.iplocator.model.IpAddress
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface IPAddressAPIService {

    @GET("{ip}")
    suspend fun getIpAddress(@Path("ip") ipAddress: String): IpAddress

}