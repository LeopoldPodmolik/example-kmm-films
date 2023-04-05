package io.visionslabs.films.android.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.visionslabs.films.android.main.screen.Screen
import io.visionslabs.films.model.Film

@Composable
fun FilmsGrid(
    navController: NavController,
    list: List<Film>?,
    @StringRes nothingText: Int,
    modifier: Modifier,
    isSearchView: Boolean = true
) {
    if (list.isNullOrEmpty()) {  // Sry no results
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(nothingText))
        }
    } else {
        val paddingValues = if (isSearchView) PaddingValues(top = 80.dp) else PaddingValues()
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = paddingValues
        ) {
            items(items = list) { item ->
                // show row with entity
                FilmItemView(
                    item = item,
                    modifier = Modifier,
                    onItemClicked = { navController.navigate("${Screen.DetailScreen.route}/${item.id}") })
            }
        }

    }

}