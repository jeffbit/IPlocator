package com.example.jeff.iplocator.network

import com.example.jeff.iplocator.model.*

const val VALID_IP = "8.8.8.8"
const val INVALID_IP = "temp"
const val VALID_TIME_INPUT = "2020-02-20T14:16:20.347071-06:00"
const val VALID_TIME_CONVERT = "02.20.2020 14:16"

val VALID_IP_RESPONSE = IpAddress(
    Asn(
        "AS15169",
        "Google LLC",
        "google.com",
        "8.8.8.0/24",
        "hosting"
    ),
    "1",
    "null",
    "NA",
    "North America",
    "0",
    "US",
    "United States",
    Currency(
        "US Dollar",
        "USD",
        "$",
        "$",
        "US dollars"
    ),
    "\ud83c\uddfa\ud83c\uddf8",
    "U+1F1FA U+1F1F8",
    "https://ipdata.co/flags/us.png",
    "8.8.8.8",
    false,
    listOf(Language("English", "English")),
    37.751,
    -97.822,
    "null",
    "null",
    "null",
    Threat(
        false,
        false,
        false,
        false,
        true,
        true,
        false
    ),
    TimeZone(
        "CST",
        "2020-02-18T21:31:05.431893-06:00",
        false,
        "America/Chicago",
        "-0600"
    )
)