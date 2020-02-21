package com.brown.jeff.iplocator.network

import com.brown.jeff.iplocator.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//creates Api
object RetrofitClientInstance {
    private const val BASE_URL =
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
    private val okHttpClient = OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()

     private fun retrofit(): Retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(
         okHttpClient
     )
        .addConverterFactory(GsonConverterFactory.create()).build()

    val ipAddressAPIService: IPAddressAPIService =
        retrofit()
            .create(IPAddressAPIService::class.java)


}
