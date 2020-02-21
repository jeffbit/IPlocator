package com.brown.jeff.iplocator.util

import com.brown.jeff.iplocator.network.Repository
import com.brown.jeff.iplocator.network.RetrofitClientInstance
import com.brown.jeff.iplocator.viewmodel.ResultScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val networkModule = module {
    //single - depicts a singleton component
    single {
        Repository(
            RetrofitClientInstance.ipAddressAPIService
        )
    }
}

//viewModel helps declare a factory instance of viewmodel(factory - wil give you a new instance each time you ask for this object type)
//get() will resolve repository instance

val viewModelResultScreen = module {
    viewModel {
        ResultScreenViewModel(
            get()
        )
    }
}

val retrofitClientModule = module {
    single { RetrofitClientInstance }

}