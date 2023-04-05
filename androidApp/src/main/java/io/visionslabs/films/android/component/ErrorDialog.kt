package io.visionslabs.films.android.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import io.visionslabs.films.android.R
import io.visionslabs.films.android.main.MainViewModel
import io.visionslabs.films.respository.RepositoryResponse

@Composable
fun ErrorDialog(data: RepositoryResponse.Error, viewModel: MainViewModel) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = stringResource(R.string.dialog_error_title))
            },
            text = {
                Text(data.message)
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        viewModel.searchFilms()
                    }) {
                    Text(stringResource(R.string.dialog_error_btn_retry))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        viewModel.searchedText = ""
                    }) {
                    Text(stringResource(R.string.dialog_error_btn_new_search))
                }
            }
        )

    }
}