package com.example.composebasics.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composebasics.consts.BOTTOM_SHEET_PADDING
import com.example.composebasics.data.MetarViewModel
import com.example.composebasics.helpers.Metar


@Composable
fun MetarSheetContent(metarViewModel: MetarViewModel) {

    Column(
        Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(MaterialTheme.colors.primary)
            .padding(all = BOTTOM_SHEET_PADDING)
    ) {

        if(metarViewModel.loadingMetar) {
            CircularProgressIndicator(
                Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colors.onPrimary
            )
        } else {
            val metar: Metar? = metarViewModel.metar
            if(metarViewModel.errorMessage.isEmpty() && metar != null) {
                Text(text = "WE IN THE SHEET FO ${metar.toString()}", color = MaterialTheme.colors.onPrimary)
            }
        }
    }
}