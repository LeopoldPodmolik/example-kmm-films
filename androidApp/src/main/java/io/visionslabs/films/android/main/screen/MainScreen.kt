package io.visionslabs.films.android.main.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import io.visionslabs.films.android.component.ErrorDialog
import io.visionslabs.films.android.component.ResultView
import io.visionslabs.films.android.component.SearchFilmView
import io.visionslabs.films.android.main.MainViewModel
import io.visionslabs.films.model.Film
import io.visionslabs.films.respository.RepositoryResponse

@Composable
fun MainScreen(
    navController: NavController,
    contentPadding: PaddingValues,
    viewModel: MainViewModel
) {
    val data by viewModel.searchResponse.observeAsState()

    Box(
        modifier = Modifier
            .padding(contentPadding)
            .padding(4.dp)
    ) {
        SearchFilmView(
            modifier = Modifier.zIndex(20f),
            onSearchBoxChanced = { viewModel.searchedText = it },
            onSearchClicked = { viewModel.searchFilms() },
            onCleanText = { viewModel.searchedText = "" }
        )
        when (data) {
            is RepositoryResponse.Loading -> { // Show loading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is RepositoryResponse.Data -> { // All is okey - lets display data
                ResultView(
                    navController = navController,
                    data = data as RepositoryResponse.Data<List<Film>>,
                    modifier = Modifier.zIndex(10f)
                )
            }
            is RepositoryResponse.Error -> { // Error
                ErrorDialog(data = data as RepositoryResponse.Error, viewModel = viewModel)
            }
            else -> {}
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
}