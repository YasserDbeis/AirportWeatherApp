package com.example.composebasics.data

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.composebasics.helpers.AirportSearchAPIService
import com.example.composebasics.models.Metar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MetarViewModel : ViewModel() {

    private var _metar: Metar? by mutableStateOf(null)
    var loadingMetar: Boolean by mutableStateOf(false)
    var errorMessage: String by mutableStateOf("")

    val metar: Metar?
        get() = _metar

    fun getMetar(icao: String) {

        loadingMetar = true
        val request = AirportSearchAPIService.getInstance()
        val call = request.getMetar(icao)

        call.enqueue(object : Callback<Metar> {
            override fun onResponse(call: Call<Metar>, response: Response<Metar>) {
                if (response.isSuccessful){
                    _metar = response.body()
                }
                loadingMetar = false
            }
            override fun onFailure(call: Call<Metar>, t: Throwable) {
                errorMessage = t.message.toString()
                loadingMetar = false
            }
        })
    }
}