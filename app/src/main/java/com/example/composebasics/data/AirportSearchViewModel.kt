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

import kotlinx.coroutines.launch

class AirportSearchViewModel : ViewModel() {
    private val _airportList = mutableStateListOf<Airport>()
    var errorMessage: String by mutableStateOf("")
    val airportList: List<Airport>
        get() = _airportList

    fun getAirportList(searchQuery: String) {
        viewModelScope.launch {
            val apiService = AirportSearchAPIService.getInstance()
            try {
                _airportList.clear()
                _airportList.addAll(apiService.getAirports(searchQuery))
                _airportList.forEach{airport ->
                    airport?.name?.let { Log.e("Yasser", it) }
                }
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}