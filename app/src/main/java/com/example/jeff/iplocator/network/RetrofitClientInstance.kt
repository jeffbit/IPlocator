package com.example.jeff.iplocator.network

import com.example.jeff.iplocator.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

//creates Api
object RetrofitClientInstance {
    private val BASE_URL =
        "https://api.ipdata.co/"

    //adds api key to every query
    private val authInterceptor = Interceptor { chain ->
        val newRequest =
            chain.request().url().newBuilder().addQueryParameter("api-key", BuildConfig.ApiKey)
                .build()
        val request = chain.request().newBuilder().url(newRequest).build()
        return@Interceptor chain.proceed(request)
    }

    //builds request url
    val okHttpClient = OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()

    val ipApi: IPAddressAPIService by lazy {
        Timber.e( "Call created")
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

        return@lazy retrofit.create(IPAddressAPIService::class.java)
    }


}
