package com.example.jeff.iplocator.util

import android.app.Application
import com.example.jeff.iplocator.network.IPAddressAPIService
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


//class to start Koin
class App : Application() {

    private lateinit var ipAddressAPIService: IPAddressAPIService
    override fun onCreate() {
        super.onCreate()
        //call to start Koin
        startKoin {
            Timber.i("Koin started")
            androidContext(this@App)
            modules(listOf(
                networkModule,
                viewModelResultScreen,
                retrofitClientModule
            ))


        }
    }
}