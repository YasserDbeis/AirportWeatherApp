package com.example.composebasics.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun res_to_str(id: Int): String {
    return stringResource(id = id)
}