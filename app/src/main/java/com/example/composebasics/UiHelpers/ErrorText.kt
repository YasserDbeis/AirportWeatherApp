package com.example.composebasics.UiHelpers

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorText(modifier: Modifier = Modifier, errorMessage: String) {
    Text(modifier = modifier, text = errorMessage, color = MaterialTheme.colors.error)
}