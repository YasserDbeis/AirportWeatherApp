package com.example.composebasics.containers
import BOTTOM_SHEET_CORNER_RADIUS
import BOTTOM_SHEET_PEEK_HEIGHT
import SURFACE_ELEVATION_DP
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composebasics.data.AirportSearchViewModel
import com.example.composebasics.data.MetarViewModel
import com.example.composebasics.home.*
import com.example.composebasics.ui.theme.ComposeBasicsTheme
import kotlinx.coroutines.launch

@Composable
fun AppContainer(content: @Composable () -> Unit) {
    ComposeBasicsTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppContentContainer(airportSearchViewModel: AirportSearchViewModel) {

    var queryString by rememberSaveable {
        mutableStateOf("")
    }

    val metarViewModel: MetarViewModel = viewModel()

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState,
    )

    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold (
        scaffoldState = scaffoldState,
        sheetContent = { MetarSheetContainer(metarViewModel) },
        sheetPeekHeight = BOTTOM_SHEET_PEEK_HEIGHT,
        sheetElevation = SURFACE_ELEVATION_DP,
        sheetShape = RoundedCornerShape(BOTTOM_SHEET_CORNER_RADIUS)
    ) {
        Column {
            SearchBar (
                queryString = queryString,
                setQueryString = { newQueryString: String ->
                    queryString = newQueryString
                },
                airportSearchViewModel = airportSearchViewModel
            ) {
                coroutineScope.launch {
                    sheetState.collapse()
                }
            }

            if(airportSearchViewModel.loadingAirports) {
                CircularProgressIndicator(
                    Modifier.align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colors.primary
                )
            } else {
                if(airportSearchViewModel.airportList.isEmpty()) {
                    AirportsListPlaceholder(noSearchResults = airportSearchViewModel.firstSearchMade)
                } else {
                    AirportList (
                        Modifier.weight(1f),
                        airportSearchViewModel,
                        onAirportClick = { icao ->
                            coroutineScope.launch {
                                metarViewModel.getMetar(icao)

                                if(sheetState.isCollapsed) {
                                    sheetState.expand()
                                }
                            }
                    })
                }

            }
        }
    }
}