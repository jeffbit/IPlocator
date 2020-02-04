package com.example.jeff.iplocator.model


import com.google.gson.annotations.SerializedName

data class IpAddress(
    val asn: Asn,
    @SerializedName("calling_code")
    val callingCode: String,
    val city: String,
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
    val postal: String,
    val region: String,
    @SerializedName("region_code")
    val regionCode: String,
    val threat: Threat,
    @SerializedName("time_zone")
    val timeZone: TimeZone
)

data class Asn(
    val asn: String,
    val domain: String,
    val name: String,
    val route: String,
    val type: String
)

data class Currency(
    val code: String,
    val name: String,
    val native: String,
    val plural: String,
    val symbol: String
)

data class Language(
    val name: String,
    val native: String
)

data class Threat(
    @SerializedName("is_anonymous")
    val isAnonymous: Boolean,
    @SerializedName("is_bogon")
    val isBogon: Boolean,
    @SerializedName("is_known_abuser")
    val isKnownAbuser: Boolean,
    @SerializedName("is_known_attacker")
    val isKnownAttacker: Boolean,
    @SerializedName("is_proxy")
    val isProxy: Boolean,
    @SerializedName("is_threat")
    val isThreat: Boolean,
    @SerializedName("is_tor")
    val isTor: Boolean
)

data class TimeZone(
    val abbr: String,
    @SerializedName("current_time")
    val currentTime: String,
    @SerializedName("is_dst")
    val isDst: Boolean,
    val name: String,
    val offset: String
)

