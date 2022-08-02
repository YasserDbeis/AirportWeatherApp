package com.example.composebasics.helpers

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

data class Airport (
    val icao: String?,
    val name: String?
)

data class Altimeter (val value: Float?)
data class Visibility (val value: Float?)
data class WindDirection (val value: Float?)
data class WindSpeed (val value: Float?)
data class DewpointDecimal(val value: Float?)
data class Temperature(val value: Float?)

data class RemarksInfo(
    @SerializedName("dewpoint_decimal")
    val dewpointDecimal: DewpointDecimal?,
    val temperature: Temperature?
)

data class Units(
    val altimeter: String?,
    val altitude: String?,
    val temperature: String?,
    val visibility: String?,
    @SerializedName("wind_speed")
    val windSpeed: String?,
)

data class Metar (
    val altimeter: Altimeter?,
    val visibility: Visibility?,
    @SerializedName("wind_name")
    val windDirection: WindDirection?,
    @SerializedName("wind_speed")
    val windSpeed: WindSpeed?,
    @SerializedName("relative_humidity")
    val relativeHumidity: Float?,
    @SerializedName("remarks_info")
    val remarksInfo: RemarksInfo?,
    val units: Units?
)

/**
 * Altimeter/Altitude: [altimeter][value]
 * visbility: [visibility][value]
 * wind direction: [wind_direction][value]
 * wind speed: [wind_speed][value]
 * relative humidity: [relative_humidity]
 * dewpoint decimal: [remarks_info][dewpoint_decimal][value]
 * temp decimal: [remarks_info][temperature][value]
 * units: [units][accumulation|altimeter|altitude|temperature|visibility|wind_speed]
 */

const val API_URL = "https://avwx.rest/api/"

interface AirportSearchAPIService {
    @Headers("Authorization: I8LSRuXgKo55emipem-Yi3Q_YI_j3_BrqWAdLQPkzvA")
    @GET("search/station")
    suspend fun getAirports(@Query("text") queryString: String): List<Airport>

    @Headers("Authorization: I8LSRuXgKo55emipem-Yi3Q_YI_j3_BrqWAdLQPkzvA")
    @GET("metar/{icao}")
    suspend fun getMetar(@Path("icao") icao: String): Metar

    companion object {
        var airportSearchAPIService: AirportSearchAPIService? = null
        fun getInstance(): AirportSearchAPIService {
            if (airportSearchAPIService == null) {
                airportSearchAPIService = Retrofit.Builder()
                    .baseUrl("$API_URL")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(AirportSearchAPIService::class.java)
            }
            return airportSearchAPIService!!
        }
    }
}