package io.visionslabs.films.network

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.visionslabs.films.model.Film
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class MovieDBApiTest {

    private val responseCorrect = """
    {
	"page": 1,
	"results": [
		{
			"adult": false,
			"backdrop_path": null,
			"genre_ids": [],
			"id": 975419,
			"original_language": "en",
			"original_title": "Marvel",
			"overview": "The quintessential student film of 1969.",
			"popularity": 2.792,
			"poster_path": "/p6XFjLX7XDnAMCczOBCevVaZpFv.jpg",
			"release_date": "1969-05-20",
			"title": "Marvel",
			"video": false,
			"vote_average": 6.542,
			"vote_count": 24
		},
		{
			"adult": false,
			"backdrop_path": "/bQl46uhGPTu9jnIRE9Ip2xOMc9M.jpg",
			"genre_ids": [
				10751,
				12,
				16,
				14,
				878
			],
			"id": 382190,
			"original_language": "ja",
			"original_title": "ポケモン・ザ・ムービーXY&Z ボルケニオンと機巧のマギアナ",
			"overview": "Ash meets the Mythical Pokémon Volcanion when it crashes down from the sky, creating a cloud of dust—and a mysterious force binds the two of them together! Volcanion despises humans and tries to get away, but it’s forced to drag Ash along as it continues its rescue mission. They arrive in a city of cogs and gears, where a corrupt official has stolen the ultimate invention: the Artificial Pokémon Magearna, created 500 years ago. He plans to use its mysterious power to take control of this mechanical kingdom! Can Ash and Volcanion work together to rescue Magearna? One of the greatest battles in Pokémon history is about to unfold!",
			"popularity": 38.351,
			"poster_path": "/j9TIzeMxNknVrBvgxzLqhIhxml4.jpg",
			"release_date": "2016-07-16",
			"title": "Pokémon the Movie: Volcanion and the Mechanical Marvel",
			"video": false,
			"vote_average": 6.688,
			"vote_count": 144
		},
		{
			"adult": false,
			"backdrop_path": "/3CxUndGhUcZdt1Zggjdb2HkLLQX.jpg",
			"genre_ids": [
				28,
				12,
				878
			],
			"id": 640146,
			"original_language": "en",
			"original_title": "Ant-Man and the Wasp: Quantumania",
			"overview": "Super-Hero partners Scott Lang and Hope van Dyne, along with with Hope's parents Janet van Dyne and Hank Pym, and Scott's daughter Cassie Lang, find themselves exploring the Quantum Realm, interacting with strange new creatures and embarking on an adventure that will push them beyond the limits of what they thought possible.",
			"popularity": 11938.691,
			"poster_path": "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
			"release_date": "2023-02-15",
			"title": "Ant-Man and the Wasp: Quantumania",
			"video": false,
			"vote_average": 6.542,
			"vote_count": 2054
		}
    ],
	"total_pages": 11,
	"total_results": 216
    }
    """.trimIndent()

    private val expectedCorrectData = listOf(
        Film(
            adult = false,
            backdropPath = "",
            genreIds = listOf(),
            id = 975419,
            originalLanguage = "en",
            originalTitle = "Marvel",
            overview = "The quintessential student film of 1969.",
            popularity = 2.792f,
            posterPath = "/p6XFjLX7XDnAMCczOBCevVaZpFv.jpg",
            releaseDate = "1969-05-20",
            title = "Marvel",
            video = false,
            voteAverage = 6.542f,
            voteCount = 24
        ),
        Film(
            adult = false,
            backdropPath = "/bQl46uhGPTu9jnIRE9Ip2xOMc9M.jpg",
            genreIds = listOf(10751, 12, 16, 14, 878),
            id = 382190,
            originalLanguage = "ja",
            originalTitle = "ポケモン・ザ・ムービーXY&Z ボルケニオンと機巧のマギアナ",
            overview = "Ash meets the Mythical Pokémon Volcanion when it crashes down from the sky, creating a cloud of dust—and a mysterious force binds the two of them together! Volcanion despises humans and tries to get away, but it’s forced to drag Ash along as it continues its rescue mission. They arrive in a city of cogs and gears, where a corrupt official has stolen the ultimate invention: the Artificial Pokémon Magearna, created 500 years ago. He plans to use its mysterious power to take control of this mechanical kingdom! Can Ash and Volcanion work together to rescue Magearna? One of the greatest battles in Pokémon history is about to unfold!",
            popularity = 38.351f,
            posterPath = "/j9TIzeMxNknVrBvgxzLqhIhxml4.jpg",
            releaseDate = "2016-07-16",
            title = "Pokémon the Movie: Volcanion and the Mechanical Marvel",
            video = false,
            voteAverage = 6.688f,
            voteCount = 144
        ),
        Film(
            adult = false,
            backdropPath = "/3CxUndGhUcZdt1Zggjdb2HkLLQX.jpg",
            genreIds = listOf(28, 12, 878),
            id = 640146,
            originalLanguage = "en",
            originalTitle = "Ant-Man and the Wasp: Quantumania",
            overview = "Super-Hero partners Scott Lang and Hope van Dyne, along with with Hope's parents Janet van Dyne and Hank Pym, and Scott's daughter Cassie Lang, find themselves exploring the Quantum Realm, interacting with strange new creatures and embarking on an adventure that will push them beyond the limits of what they thought possible.",
            popularity = 11938.691f,
            posterPath = "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
            releaseDate = "2023-02-15",
            title = "Ant-Man and the Wasp: Quantumania",
            video = false,
            voteAverage = 6.542f,
            voteCount = 2054
        )
    )

    @Test
    fun getSearchResult_correct() {
        runBlocking {
            val mockEngine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(responseCorrect),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val apiClient = MovieDBApi(mockEngine)

            Assert.assertEquals(
                expectedCorrectData,
                apiClient.getMovieSearch(
                    query = "marvel"
                ).results
            )
        }
    }


}