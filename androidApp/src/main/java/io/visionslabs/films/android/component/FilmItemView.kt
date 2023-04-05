package io.visionslabs.films.android.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.visionslabs.films.android.R
import io.visionslabs.films.model.Film
import java.text.SimpleDateFormat

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FilmItemView(
    item: Film,
    modifier: Modifier,
    onItemClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(percent = 10))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable { onItemClicked() }
    ) {

        val imageModifier: Modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 150.dp)
        // Image of film poster
        if (item.posterPath.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.poster_placeholder),
                contentDescription = "Image",
                modifier = imageModifier
            )
        } else {
            val posterRootURL = "https://image.tmdb.org/t/p/original"
            GlideImage(
                model = posterRootURL + item.posterPath,
                contentDescription = "Image",
                modifier = imageModifier,
                contentScale = ContentScale.Crop,
            )
        }
        // Title and year of release
        Column(
            modifier = Modifier
                .padding(12.dp)
                .heightIn(min = 62.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (item.releaseDate.isNotEmpty()) {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf.parse(item.releaseDate)
                    ?.let { SimpleDateFormat.getDateInstance().format(it) }
                Text(text = date.toString(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(widthDp = 150)
@Composable
fun FilmItemPreview() {
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
        voteCount = 4101
    )
    FilmItemView(item = item, modifier = Modifier.padding(top = 50.dp)) {}
}