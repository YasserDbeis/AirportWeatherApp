package com.example.composebasics.home
import SEARCH_BAR_ERROR_MSG_START_PADDING
import SEARCH_BAR_PADDING
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.composebasics.R
import com.example.composebasics.UiHelpers.ErrorText
import com.example.composebasics.consts.MIN_SEARCH_LEN
import com.example.composebasics.data.AirportSearchViewModel

@Composable
fun SearchBar(
    modifier: Modifier = Modifier.background(Color.Transparent),
    queryString: String,
    setQueryString: (String) -> Unit,
    airportSearchViewModel: AirportSearchViewModel,
    collapseBottomSheet: () -> Unit
)
{
    val focusManager = LocalFocusManager.current
    var error by remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(all = SEARCH_BAR_PADDING)
        ,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onPrimary,
            disabledTextColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.primary,
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
        leadingIcon = { Icon(
            Icons.Outlined.Search, "Search", tint = MaterialTheme.colors.onPrimary
        )
        },
        trailingIcon = {
            if(error) {
                Icon(
                    Icons.Outlined.Info,
                    "Error",
                    tint = MaterialTheme.colors.error
                )
            }
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            if(queryString.length < MIN_SEARCH_LEN) {
                error = true
            } else {
                airportSearchViewModel.getAirportList(queryString)
                focusManager.clearFocus()

                collapseBottomSheet()
            }
        }),
        placeholder = { Text(text = "Search", color = MaterialTheme.colors.onPrimary) }
    )
    if(error) {
        val errorMessage = stringResource(id = R.string.three_char_error_msg)
        ErrorText(
            modifier = Modifier.padding(start = SEARCH_BAR_ERROR_MSG_START_PADDING),
            errorMessage = errorMessage
        )
    }
}
