package com.example.composebasics

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebasics.ui.theme.ComposeBasicsTheme
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composebasics.data.AirportSearchViewModel
import com.example.composebasics.data.MetarViewModel
import com.example.composebasics.helpers.Airport
import com.example.composebasics.helpers.Metar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val airportSearchViewModel: AirportSearchViewModel = viewModel()
            MyApp { MyScreenContent(airportSearchViewModel) }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    ComposeBasicsTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyScreenContent(airportSearchViewModel: AirportSearchViewModel) {

    var queryString by rememberSaveable {
        mutableStateOf("")
    }

    var selectedIcao: String? by remember {
        mutableStateOf(null)
    }

    val metarViewModel: MetarViewModel = viewModel()

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
//        Log.e("METAR", bottomSheetState.name)
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState,
    )

    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {

            MetarSheetContent(metarViewModel)

        },
        sheetPeekHeight = 50.dp,
        sheetElevation = 20.dp,
        sheetShape = RoundedCornerShape(16.dp)
        ) {
        Column {
            SearchBar(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                queryString = queryString,
                setQueryString = { newQueryString: String ->
                    queryString = newQueryString
                },
                airportSearchViewModel = airportSearchViewModel,
                collapseBottomSheet = {
                    coroutineScope.launch {
                        sheetState.collapse()
                    }
                }
            )

            if(airportSearchViewModel.loadingAirports) {
                CircularProgressIndicator(
                    Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Red
                )
            } else {
                AirportList(Modifier.weight(1f), airportSearchViewModel, onAirportClick = {icao ->

                    coroutineScope.launch {

                        metarViewModel.getMetar(icao)
                        selectedIcao = icao

                        if(sheetState.isCollapsed) {
                            sheetState.expand()
                        }

                    }
                })
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBar(modifier: Modifier, queryString: String, setQueryString: (String) -> Unit, airportSearchViewModel: AirportSearchViewModel, collapseBottomSheet: () -> Unit) {
    val focusManager = LocalFocusManager.current
    var error by remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
        ,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            backgroundColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(20.dp),
        value = queryString,
        onValueChange = {
            setQueryString(it)
            error = false
        },
        leadingIcon = {Icon(Icons.Outlined.Search, "Search")},
        trailingIcon = { if(error) Icon(Icons.Filled.Info, "Error", tint = MaterialTheme.colors.error) },
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            if(queryString.length < 3) {
                error = true
            } else {
                airportSearchViewModel.getAirportList(queryString)
                focusManager.clearFocus()

                collapseBottomSheet()
            }
        }),
        placeholder = {Text(text = "Search")}
    )
    if(error) {
        Text(text = "Search must have at least 3 characters", color = Color.Red)
    }

}

@Composable 
fun AirportList(
    modifier: Modifier,
    airportSearchViewModel: AirportSearchViewModel,
    onAirportClick: (icao: String) -> Unit
) {
    Log.e("Yasser error", airportSearchViewModel.errorMessage)
    Log.e("Yasser error", airportSearchViewModel.airportList.size.toString())

    if(airportSearchViewModel.errorMessage.isEmpty() && airportSearchViewModel.airportList.isNotEmpty()) {
        val airportSearchResults = airportSearchViewModel.airportList

        for (air in airportSearchResults) {
            Log.e("AIRPORTS", air.toString())
        }

        Log.e("AIRPORTS", "----------")

        LazyColumn() {
            items(items = airportSearchResults) {airport ->
                if(!airport.name.isNullOrBlank() && !airport.icao.isNullOrBlank()) {

                    Card(
                        shape = RoundedCornerShape(10.dp),
                        elevation = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp)
                        ) {
                        Column(
                            modifier = Modifier
                                .clickable { onAirportClick(airport.icao) }
                                .padding(10.dp)

                        ) {
                            Text(fontWeight = FontWeight.Bold, text = airport.name)
                            Text(text = airport.icao)
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun MetarSheetContent(metarViewModel: MetarViewModel) {

    Column(
        Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(Color.Green)
            .padding(20.dp)
    ) {

        if(metarViewModel.loadingMetar) {
            CircularProgressIndicator(
                Modifier.align(Alignment.CenterHorizontally),
                color = Color.Red
            )
        } else {
            val metar: Metar? = metarViewModel.metar
            Log.e("METAR", metar.toString())

            if(metarViewModel.errorMessage.isNullOrBlank() && metar != null) {
                Text(text = "WE IN THE SHEET FO ${metar.toString()}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val airportSearchViewModel: AirportSearchViewModel = viewModel()
    MyApp { MyScreenContent(airportSearchViewModel = airportSearchViewModel) }
}