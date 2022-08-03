package com.example.composebasics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composebasics.containers.AppContainer
import com.example.composebasics.containers.AppContentContainer
import com.example.composebasics.data.AirportSearchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val airportSearchViewModel: AirportSearchViewModel = viewModel()
            AppContainer { AppContentContainer(airportSearchViewModel) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val airportSearchViewModel: AirportSearchViewModel = viewModel()
    AppContainer { AppContentContainer(airportSearchViewModel = airportSearchViewModel) }
}