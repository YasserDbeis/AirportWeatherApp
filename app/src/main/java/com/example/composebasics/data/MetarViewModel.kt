package com.example.composebasics.data

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebasics.helpers.Airport
import com.example.composebasics.helpers.AirportSearchAPIService
import com.example.composebasics.helpers.Metar

import kotlinx.coroutines.launch



class MetarViewModel : ViewModel() {
    private var _metar: Metar? by mutableStateOf(null)
    var errorMessage: String by mutableStateOf("")
    val metar: Metar?
        get() = _metar

    fun getMetar(icao: String) {
        viewModelScope.launch {
            val apiService = AirportSearchAPIService.getInstance()
            try {
                _metar = apiService.getMetar(icao)
                Log.e("METAR 2", _metar.toString())

            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}