package io.visionslabs.films.android.main.screen

import android.icu.text.NumberFormat
import android.icu.util.Currency
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.visionslabs.films.android.R
import io.visionslabs.films.android.main.MainViewModel
import io.visionslabs.films.model.Film
import io.visionslabs.films.respository.RepositoryResponse
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

@Composable
fun DetailScreen(
    navController: NavController,
    contentPadding: PaddingValues,
    viewModel: MainViewModel,
    detailId: Int
) {

    viewModel.getMovieDetail(id = detailId)
    viewModel.isFavorited(id = detailId)
    val data by viewModel.movieResponse.observeAsState()

    Column(
        modifier = Modifier
            .padding(contentPadding)
            .padding(4.dp)
            .fillMaxWidth()
    ) {

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
                Detail(
                    film = (data as RepositoryResponse.Data<Film>).value,
                    isFavorite = viewModel.isFavoritedFilm,
                    addToFavorites = { viewModel.addToFavorites(it) },
                    removeFromFavorites = { viewModel.removeFromFavorites(it) }
                )
            }
            is RepositoryResponse.Error -> { // Error
                Text(text = stringResource(id = R.string.screen_detail_no_data))
            }
            else -> {}
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Detail(
    film: Film,
    isFavorite: LiveData<Boolean>,
    addToFavorites: (Film) -> Unit,
    removeFromFavorites: (Int) -> Unit
) {
    val myAppIcons = Icons.Outlined
    val isFav by isFavorite.observeAsState()
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = film.title, style = MaterialTheme.typography.headlineLarge)

        val imageModifier: Modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
        // Image of movie poster
        if (film.posterPath.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.poster_placeholder),
                contentDescription = "Image",
                modifier = imageModifier
            )
        } else {
            val posterRootURL = "https://image.tmdb.org/t/p/original"
            GlideImage(
                model = posterRootURL + film.posterPath,
                contentDescription = "Image",
                modifier = imageModifier.clip(shape = RoundedCornerShape(5)),
                contentScale = ContentScale.Crop,
            )
        }
        Column(modifier = Modifier) {
            // More info
            Card(modifier = Modifier.padding(vertical = 8.dp)) {
                Row {
                    Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                        // Release date
                        if (film.releaseDate.isNotEmpty()) {
                            val sdf = SimpleDateFormat("yyyy-MM-dd")
                            val date = sdf.parse(film.releaseDate)
                                ?.let { SimpleDateFormat.getDateInstance().format(it) }
                            DetailInfoItem(text = date.toString(), icon = myAppIcons.Done)
                        }
                        // Budget in USD
                        DetailInfoItem(text = moneyFormatter(film.budget), icon = myAppIcons.Star)
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Vote in %
                        DetailInfoItem(
                            text = (film.voteAverage * 10).roundToInt().toString() + "%",
                            icon = myAppIcons.ThumbUp
                        )
                        // Revenue in USD
                        DetailInfoItem(text = moneyFormatter(film.revenue), icon = myAppIcons.Add)
                    }
                }
            }
            Text(text = film.overview)
        }
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (isFav == true) {
                Button(onClick = { removeFromFavorites(film.id) }) {
                    Icon(
                        myAppIcons.Delete,
                        contentDescription = "Remove from favorite",
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.screen_detail_remove_favorite),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            } else {
                Button(onClick = { addToFavorites(film) }) {
                    Icon(
                        myAppIcons.Favorite,
                        contentDescription = "Add to favorite",
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.screen_detail_add_favorite),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Format number tu currency string
 */
private fun moneyFormatter(number: Long): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("USD")

    return format.format(number)
}

@Composable
fun DetailInfoItem(icon: ImageVector, text: String, contentDesc: String = "") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = contentDesc,
            modifier = Modifier.padding(end = 4.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }

}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun DetailPreview() {
    val item = Film(
        adult = false,
        backdropPath = "/xDMIl84Qo5Tsu62c9DGWhmPI67A.jpg",
        genreIds = listOf(28, 12, 878),
        id = 505642,
        originalLanguage = "en",
        originalTitle = "Black Panther: Wakanda Forever",
        overview = "Queen Ramonda, Shuri, M’Baku, Okoye and the Dora Milaje fight to protect their nation from intervening world powers in the wake of King T’Challa’s death.  As the Wakandans strive to embrace their next chapter, the heroes must band together with the help of War Dog Nakia and Everett Ross and forge a new path for the kingdom of Wakanda.",
        popularity = 1798.789f,
//        posterPath = "/sv1xJUazXeYqALzczSZ3O6nkH75.jpg",
        posterPath = "",
        releaseDate = "2022-11-09",
        title = "Black Panther: Wakanda Forever",
        video = false,
        voteAverage = 7.335f,
        voteCount = 4101,
        budget = 250000000,
        revenue = 858535561
    )
    Detail(
        film = item,
        addToFavorites = {},
        removeFromFavorites = {},
        isFavorite = MutableLiveData(true)
    )
}