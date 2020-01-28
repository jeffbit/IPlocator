package com.example.jeff.iplocator.model


import com.google.gson.annotations.SerializedName

data class IpAddress(
    val asn: Asn,
    @SerializedName("calling_code")
    val callingCode: String,
    val city: Any,
    @SerializedName("continent_code")
    val continentCode: String,
    @SerializedName("continent_name")
    val continentName: String,
    val count: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("country_name")
    val countryName: String,
    val currency: Currency,
    @SerializedName("emoji_flag")
    val emojiFlag: String,
    @SerializedName("emoji_unicode")
    val emojiUnicode: String,
    val flag: String,
    val ip: String,
    @SerializedName("is_eu")
    val isEu: Boolean,
    val languages: List<Language>,
    val latitude: Double,
    val longitude: Double,
    val postal: Any,
    val region: Any,
    @SerializedName("region_code")
    val regionCode: Any,
    val threat: Threat,
    @SerializedName("time_zone")
    val timeZone: TimeZone
)

