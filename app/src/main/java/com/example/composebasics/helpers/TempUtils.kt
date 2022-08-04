package com.example.composebasics.helpers

import com.example.composebasics.R

fun c_to_f(c: Float): Float {
    return (c * 9) / 5 + 32
}
//(0°C × 9/5) + 32 = 32°F

fun getTemperatureIcon(tempValue: Float): Int {
    return if(tempValue <= 15) {
        // Cold
        R.drawable.cold_therm
    } else {
        // Warm/Hot
        R.drawable.hot_therm
    }
}

fun getDewpointIcon(tempValue: Float): Int {
    return R.drawable.dew
}

fun getWindSpeedIcon(speedValue: Float): Int {
    return R.drawable.wind
}

fun getWindDirectionIcon(windDirValue: Float): Int {
    return R.drawable.compass
}

fun getVisibilityIcon(visibilityValue: Float): Int {
    return R.drawable.cloudy
}

fun getAltitudeIcon(altitudeValue: Float): Int {
    return R.drawable.mountains
}