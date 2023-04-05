package io.visionslabs.films.android.main.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.visionslabs.films.android.R
import io.visionslabs.films.android.component.FilmsGrid
import io.visionslabs.films.android.main.MainViewModel

@Composable
fun FavoriteScreen(
    navController: NavController,
    contentPadding: PaddingValues,
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .padding(4.dp)
    ) {
        viewModel.getFavoriteFilms()
        val list by viewModel.favoriteFilms.observeAsState()
        FilmsGrid(
            navController = navController,
            list = list,
            modifier = Modifier,
            nothingText = R.string.view_result_no_data
        )
    }
}