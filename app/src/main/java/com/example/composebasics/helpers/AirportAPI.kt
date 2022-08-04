package com.example.composebasics.helpers

import android.os.Build
import com.example.composebasics.BuildConfig
import com.example.composebasics.models.Airport
import com.example.composebasics.models.Metar
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val API_URL = "https://avwx.rest/api/"

interface AirportSearchAPIService {

    @Headers("Authorization: ${BuildConfig.AVWX_AUTH_KEY}")
    @GET("search/station")
    fun getAirports(@Query("text") queryString: String): Call<List<Airport>>

    @Headers("Authorization: ${BuildConfig.AVWX_AUTH_KEY}")
    @GET("metar/{icao}")
    fun getMetar(@Path("icao") icao: String): Call<Metar>

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