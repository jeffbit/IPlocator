package com.example.jeff.iplocator.model


import com.google.gson.annotations.SerializedName

data class Currency(
    val code: String,
    val name: String,
    val native: String,
    val plural: String,
    val symbol: String
)