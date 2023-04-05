package io.visionslabs.films.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.visionslabs.films.Constant
import io.visionslabs.films.model.Film
import kotlinx.serialization.json.Json

class MovieDBApi(engine: HttpClientEngine = CIO.create()) {
    private val apiBase = "https://api.themoviedb.org/3"

    /**
     * Base HTTP client with settings
     */
    private val httpClient = HttpClient(engine) {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(HttpCache)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
                coerceInputValues = true
            })
        }
        expectSuccess = true
    }

    /**
     * Call for searching movies
     */
    suspend fun getMovieSearch(query: String): ResponseSearch {
        return httpClient.get("$apiBase/search/movie") {
            url {
                parameters.apply {
                    append("api_key", Constant.APIKEY)
                    append("query", query)
                    append("include_adult", false.toString())
                }
            }
        }.body()
    }

    /**
     * Call for one film - details
     */
    suspend fun getMovie(id: Int): Film {
        return httpClient.get("$apiBase/movie/$id") {
            url {
                parameters.apply {
                    append("api_key", Constant.APIKEY)
                }
            }
        }.body()
    }

}