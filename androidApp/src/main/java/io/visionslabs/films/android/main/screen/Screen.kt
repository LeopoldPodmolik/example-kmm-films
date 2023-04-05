package io.visionslabs.films.android.main.screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import io.visionslabs.films.android.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int = R.string.screen_title_search,
    val icon: ImageVector = Icons.Filled.Favorite
) {
    object MainScreen :
        Screen(route = "mainScreen", R.string.screen_title_search, icon = Icons.Filled.Search)

    object FavoriteScreen : Screen(
        route = "favoriteScreen",
        R.string.screen_title_favorite,
        icon = Icons.Filled.Favorite
    )

    object DetailScreen : Screen(route = "detailScreen")
}
