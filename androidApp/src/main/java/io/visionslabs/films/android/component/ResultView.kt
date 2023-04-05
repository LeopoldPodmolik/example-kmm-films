package io.visionslabs.films.android.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.visionslabs.films.android.R
import io.visionslabs.films.model.Film
import io.visionslabs.films.respository.RepositoryResponse

@Composable
fun ResultView(
    navController: NavController,
    data: RepositoryResponse.Data<List<Film>>,
    modifier: Modifier,
    isSearchView: Boolean = true
) {
    val list = data.value
    FilmsGrid(
        navController = navController,
        modifier = modifier,
        isSearchView = isSearchView,
        list = list,
        nothingText = R.string.view_result_no_data
    )
}