package com.example.composebasics.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebasics.helpers.Airport
import com.example.composebasics.helpers.AirportSearchAPIService
import com.example.composebasics.helpers.Metar

import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AirportSearchViewModel() : ViewModel() {
    private val _airportList = mutableStateListOf<Airport>()
    var errorMessage: String by mutableStateOf("")
    var loadingAirports: Boolean by mutableStateOf(false)

    val airportList: List<Airport>
        get() = _airportList

    fun getAirportList(searchQuery: String) {

        loadingAirports = true

        val request = AirportSearchAPIService.getInstance()
        val call = request.getAirports(searchQuery)

        call.enqueue(object : Callback<List<Airport>> {
            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) {
                _airportList.clear()
                response.body().let {
                    if (it != null) {
                        _airportList.addAll(it)
                    }
                }
                loadingAirports = false
            }
            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                errorMessage = t.message.toString()
                loadingAirports = false
            }
        })
    }
}