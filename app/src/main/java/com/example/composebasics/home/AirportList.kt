package com.example.composebasics.home

import CARD_ELEVATION_DP
import CARD_INNER_PADDING
import CARD_OUTER_PADDING
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.composebasics.data.AirportSearchViewModel


@Composable
fun AirportList(
    modifier: Modifier,
    airportSearchViewModel: AirportSearchViewModel,
    onAirportClick: (icao: String) -> Unit
) {

    if(airportSearchViewModel.errorMessage.isEmpty()) {
        val airportSearchResults = airportSearchViewModel.airportList

        LazyColumn() {
            items(items = airportSearchResults) {airport ->
                if(!airport.name.isNullOrBlank() && !airport.icao.isNullOrBlank()) {

                    Card(
                        shape = RoundedCornerShape(10.dp),
                        elevation = CARD_ELEVATION_DP,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = CARD_OUTER_PADDING)
                    ) {
                        Column(
                            modifier = Modifier
                                .clickable { onAirportClick(airport.icao) }
                                .padding(CARD_INNER_PADDING)

                        ) {
                            Text(text = airport.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onSurface)
                            Text(text = airport.icao, color = MaterialTheme.colors.onSurface)
                        }
                    }

                }
            }
        }
    }
}
