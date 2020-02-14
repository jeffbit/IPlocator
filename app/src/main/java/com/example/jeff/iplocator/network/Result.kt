package com.example.jeff.iplocator.network

import com.example.jeff.iplocator.model.IpAddress

sealed class Result<out T> {
    class Loading<out T> : Result<T>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val throwable: Throwable) : Result<T>()
    //Todo: figure out how to properly set up with nullvalue
    data class NullValue<out T>(val data: IpAddress? = null) : Result<T>()

}


