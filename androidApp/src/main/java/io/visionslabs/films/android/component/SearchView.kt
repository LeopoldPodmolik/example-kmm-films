package io.visionslabs.films.android.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.visionslabs.films.android.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchFilmView(
    modifier: Modifier,
    onSearchBoxChanced: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onCleanText: () -> Unit
) {
    var localText by remember { mutableStateOf("") }
    val isSomeText by remember {
        derivedStateOf { localText.isNotBlank() }
    }


    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            TextField(
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 50))
                    .weight(1f),
                value = localText,
                onValueChange = {
                    onSearchBoxChanced(it)
                    localText = it
                },
                singleLine = true,
                placeholder = { Text(stringResource(R.string.view_search_placeholder)) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (isSomeText) {
                        IconButton(onClick = {
                            onCleanText()
                            localText = ""
                        }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear text")
                        }
                    }
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        onSearchClicked()
                        keyboardController?.hide()
                    })
            )
        }
    }
}

@Preview
@Composable
fun SearchSportPreview() {
    SearchFilmView(
        modifier = Modifier,
        onSearchBoxChanced = { },
        onSearchClicked = {},
        onCleanText = {}
    )
}