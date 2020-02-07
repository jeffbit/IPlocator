package com.example.jeff.iplocator.util

import com.example.jeff.iplocator.network.Repository
import com.example.jeff.iplocator.network.RetrofitClientInstance
import com.example.jeff.iplocator.viewmodel.SearchScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val networkModule = module {
    //single - depicts a singleton component
    single<Repository> { Repository(get()) }
}

//viewModel helps declare a factory instance of viewmodel(factory - wil give you a new instance each time you ask for this object type)
//get() will resolve repository instance

val viewModelModule = module {
    viewModel { SearchScreenViewModel(get()) }
}

val retrofitClientModule = module {
    single<RetrofitClientInstance> { RetrofitClientInstance }

}