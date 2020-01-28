package com.example.jeff.iplocator.model


import com.google.gson.annotations.SerializedName

data class Asn(
    val asn: String,
    val domain: String,
    val name: String,
    val route: String,
    val type: String
)