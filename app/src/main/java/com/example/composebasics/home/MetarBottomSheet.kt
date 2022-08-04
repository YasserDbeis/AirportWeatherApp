package com.example.composebasics.home

import BOTTOM_SHEET_PADDING
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composebasics.UiHelpers.ErrorText
import com.example.composebasics.data.MetarViewModel
import com.example.composebasics.helpers.*
import com.example.composebasics.models.Metar


@Composable
fun MetarSheetContainer(metarViewModel: MetarViewModel) {

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.60f)
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
            val errorMessage = metarViewModel.errorMessage
            MetarSheetContentContainer(metar = metar, errorMessage = errorMessage)

        }
    }
}

@Composable
fun MetarSheetContentContainer(metar: Metar?, errorMessage: String) {

    if(errorMessage.isNotEmpty()) {
        ErrorText(errorMessage = errorMessage)
    } else if(metar == null) {
        Text(text = "No Airport Selected", color = MaterialTheme.colors.onPrimary)
    } else {
        MetarSheetContent(metar = metar)
    }
}

/**
data class Metar (
    val altimeter: Altimeter?,
    val visibility: Visibility?,
    @SerializedName("wind_direction")
    val windDirection: WindDirection?,
    @SerializedName("wind_speed")
    val windSpeed: WindSpeed?,
    @SerializedName("relative_humidity")
    val relativeHumidity: Float?,
    val dewpoint: Dewpoint?,
    val temperature: Temperature?,
    val units: Units?
)

data class Altimeter (val value: Float?)
data class Visibility (val value: Float?)
data class WindDirection (val value: Float?)
data class WindSpeed (val value: Float?)
data class Dewpoint(val value: Float?)
data class Temperature(val value: Float?)

data class Units (
    val altimeter: String?,
    val altitude: String?,
    val temperature: String?,
    val visibility: String?,
    @SerializedName("wind_speed")
    val windSpeed: String?,
)
 */



@Composable
fun MetarSheetContent(metar: Metar) {

    Column() {

        Column (Modifier.verticalScroll(rememberScrollState()).weight(1f, false)){
            val metarEntryModifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)

            metar.temperature?.value?.let {
                MetarContentEntry(
                    modifier = metarEntryModifier,
                    metarInfoType = MetarInfoType.Temperature,
                    value = it,
                    unit = metar.units?.temperature
                )
                Divider()
            }

            metar.dewpoint?.value?.let {
                MetarContentEntry(
                    modifier = metarEntryModifier,
                    metarInfoType = MetarInfoType.Dewpoint,
                    value = it,
                    unit = metar.units?.temperature
                )
                Divider()
            }
            metar.windSpeed?.value?.let {
                MetarContentEntry(
                    modifier = metarEntryModifier,
                    metarInfoType = MetarInfoType.WindSpeed,
                    value = it,
                    unit = metar.units?.windSpeed
                )
                Divider()
            }
            metar.windDirection?.value?.let {
                MetarContentEntry(
                    modifier = metarEntryModifier,
                    metarInfoType = MetarInfoType.WindDirection,
                    value = it,
                    unit = null
                )
                Divider()
            }
            metar.visibility?.value?.let {
                MetarContentEntry(
                    modifier = metarEntryModifier,
                    metarInfoType = MetarInfoType.Visibility,
                    value = it,
                    unit = metar.units?.visibility
                )
            }
        }
    }
}

enum class MetarInfoType {
    Temperature,
    Dewpoint,
    WindSpeed,
    WindDirection,
    Visibility,
}

enum class TemperatureType {
    General,
    Dewpoint
}

@Composable
fun MetarContentEntry(modifier: Modifier = Modifier, metarInfoType: MetarInfoType, value: Float, unit: String?) {

    val (title, info, icon) = when(metarInfoType) {
        MetarInfoType.Temperature -> TemperatureContent(tempType = TemperatureType.General, tempValue = value, unit = unit)
        MetarInfoType.Dewpoint -> TemperatureContent(tempType = TemperatureType.Dewpoint, tempValue = value, unit = unit)
        MetarInfoType.WindSpeed -> WindSpeedContent(speedValue = value, unit = unit)
        MetarInfoType.WindDirection -> WindDirectionContent(windDirValue = value, unit = unit)
        MetarInfoType.Visibility -> VisibilityContent(visibilityValue = value, unit = unit)
    }

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.align(Alignment.CenterVertically)) {
            Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                text = info,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 18.sp
            )
        }

        Image(
            modifier = Modifier,
            painter = painterResource(icon),
            contentDescription = "Temperature Icon"
        )
    }
}

@Composable
fun TemperatureContent(modifier: Modifier = Modifier, tempType: TemperatureType, tempValue: Float, unit: String?): Triple<String, String, Int> {

    val (title, icon) = when(tempType) {
        TemperatureType.General -> Pair("Temperature", getTemperatureIcon(tempValue))
        TemperatureType.Dewpoint -> Pair("Dew Point", getDewpointIcon(tempValue))
    }

    val celcTemp = tempValue.toInt()
    val farTemp = c_to_f(tempValue).toInt()

    val info = "${celcTemp}°$unit | ${farTemp}°F"

    return Triple(title, info, icon)
}

@Composable
fun WindSpeedContent(speedValue: Float, unit: String?): Triple<String, String, Int> {

    val title = "Wind Speed"
    val icon = getWindSpeedIcon(speedValue)

    var info = "${speedValue.toInt()}"
    if(unit != null) {
        info += " $unit"
    }

    return Triple(title, info, icon)
}

@Composable
fun WindDirectionContent(windDirValue: Float, unit: String?): Triple<String, String, Int> {

    val title = "Wind Direction"
    val icon = getWindDirectionIcon(windDirValue)

    var info = "${windDirValue.toInt()}°"
    if(unit != null) {
        info += " $unit"
    }

    return Triple(title, info, icon)
}

@Composable
fun VisibilityContent(visibilityValue: Float, unit: String?): Triple<String, String, Int> {
    val title = "Visibility"
    val icon = getVisibilityIcon(visibilityValue)

    var info = "${visibilityValue.toInt()}"
    if(unit != null) {
        info += " $unit"
    }

    return Triple(title, info, icon)
}
