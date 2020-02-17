package com.example.jeff.iplocator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchScreenViewModel : ViewModel() {


    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery


    fun setSearchQuery(query: String? = null) {
        _searchQuery.postValue(query)
    }

}






