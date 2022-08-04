package com.example.composebasics.home

import AIRPLANE_ICON_PADDING
import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.composebasics.R
import com.example.composebasics.helpers.res_to_str

@Composable
fun AirportsListPlaceholder(noSearchResults: Boolean) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        if(noSearchResults) {
            Text(text = res_to_str(id = R.string.no_airports_found_msg), color = MaterialTheme.colors.error)
        } else {
            Column {

                Image(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(AIRPLANE_ICON_PADDING),
                    painter = painterResource(R.drawable.plane),
                    contentDescription = "Search for an airport to begin!"
                )
                Text(
                    text = "Search for an airport to begin!",
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}