package com.example.composebasics.models

import com.google.gson.annotations.SerializedName

data class Metar (

    val visibility: Visibility?,
    @SerializedName("wind_direction")
    val windDirection: WindDirection?,
    @SerializedName("wind_speed")
    val windSpeed: WindSpeed?,
    @SerializedName("relative_humidity")
    val relativeHumidity: Float?,
    val dewpoint: Dewpoint?,
    val temperature: Temperature?,
    val units: Units?
)

data class Visibility (val value: Float?)
data class WindDirection (val value: Float?)
data class WindSpeed (val value: Float?)
data class Dewpoint(val value: Float?)
data class Temperature(val value: Float?)

data class Units (

    val temperature: String?,
    val visibility: String?,
    @SerializedName("wind_speed")
    val windSpeed: String?,
)

