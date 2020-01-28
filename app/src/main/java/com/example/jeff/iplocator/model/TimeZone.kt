package com.example.jeff.iplocator.model


import com.google.gson.annotations.SerializedName

data class TimeZone(
    val abbr: String,
    @SerializedName("current_time")
    val currentTime: String,
    @SerializedName("is_dst")
    val isDst: Boolean,
    val name: String,
    val offset: String
)