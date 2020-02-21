package com.brown.jeff.iplocator.network

import com.brown.jeff.iplocator.model.IpAddress
import timber.log.Timber

class Repository(
    private val ipAddressAPIService: IPAddressAPIService
) {


    suspend fun getIp(ip: String): IpAddress {
        Timber.e("Get IP: %s", ip)
        return ipAddressAPIService.getIpAddress(ip)


    }


}