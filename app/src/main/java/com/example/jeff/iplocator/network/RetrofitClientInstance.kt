package com.example.jeff.iplocator.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//creates Api
//"https://api.ipdata.co/1.1.1.1?api-key=afcddd6bfbb6a79d0dfad606fad449bbb491cd07f89833d312ae16e7
object RetrofitClientInstance {
    private val BASE_URL =
        "https://api.ipdata.co/"
    private const val APIKEY = "afcddd6bfbb6a79d0dfad606fad449bbb491cd07f89833d312ae16e7"


    //adds api key to every query
    private val authInterceptor = Interceptor { chain ->
        val newRequest =
            chain.request().url().newBuilder().addQueryParameter("api-key", APIKEY).build()
        val request = chain.request().newBuilder().url(newRequest).build()
        return@Interceptor chain.proceed(request)
    }

    //builds request url
    val okHttpClient = OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()

    //lazy means that it will only execute the first time
    val ipApi: IPAddressAPIService by lazy {
        Log.e("RetrofitCall", "Creating Client")
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
        Log.e("RETROFIT_CLIENT", okHttpClient.authenticator().toString())

        return@lazy retrofit.create(IPAddressAPIService::class.java)
    }


}