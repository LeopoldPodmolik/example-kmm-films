package io.visionslabs.films.respository

import io.visionslabs.films.database.Database
import io.visionslabs.films.model.Film
import io.visionslabs.films.network.MovieDBApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.*
import kotlin.time.Duration.Companion.minutes

class Repository(
    private val api: MovieDBApi, private val database: Database
) {

    /**
     * Store for Search
     */
    private val searchStore: Store<String, List<Film>> =
        StoreBuilder.from<String, List<Film>, List<Film>>(fetcher = Fetcher.of { query: String -> api.getMovieSearch(query).results })
            .cachePolicy(
                MemoryPolicy.builder<Any, Any>()
                    .setExpireAfterAccess(10.minutes) // or setExpireAfterWrite(10.minutes)
                    .build()
            ).build()

    /**
     * Store for Search
     */
    private val movieDetailStore: Store<Int, Film> =
        StoreBuilder.from<Int, Film, Film>(fetcher = Fetcher.of { id: Int -> api.getMovie(id) })
            .cachePolicy(
                MemoryPolicy.builder<Any, Any>()
                    .setExpireAfterAccess(10.minutes) // or setExpireAfterWrite(10.minutes)
                    .build()
            ).build()

    /**
     * Store for movie search
     */
    fun searchMovieFlow(query: String): Flow<RepositoryResponse<List<Film>>> {
        return searchStore.stream(StoreReadRequest.cached(query, false))
            .map { response -> transformResponse(response) }
    }

    /**
     * Store for movie detail
     */
    fun getMovieDetailFlow(id: Int): Flow<RepositoryResponse<Film>> {
        return movieDetailStore.stream(StoreReadRequest.cached(id, false))
            .map { response -> transformResponse(response) }
    }

    /**
     * Save film to DB
     */
    fun saveToFavorites(film: Film) {
        database.insertFilm(film)
    }

    /**
     * Delete film from DB
     */
    fun removeFromFavorites(id: Int) {
        database.removeFilm(id)
    }

    /**
     * Get one film from DB
     */
    fun getOneFilm(id: Int): Film? {
        val film = database.getFilm(id)
        return film
    }

    /**
     * Get all films from DB
     */
    fun getAllFavoritesFilm(): List<Film> {
        return database.getAllFavoritesFilms()
    }

    /**
     * Transform response from API to store custom entity
     */
    private fun <T> transformResponse(response: StoreReadResponse<T>): RepositoryResponse<T> {
        return when(response) {
            is StoreReadResponse.Loading -> RepositoryResponse.Loading(response.origin.name)
            is StoreReadResponse.Data -> RepositoryResponse.Data(value = response.value, origin = response.origin.name)
            is StoreReadResponse.Error -> {
                when(response) {
                    is StoreReadResponse.Error.Exception -> RepositoryResponse.Error(origin = response.origin.name, message = response.error.message ?: "error")
                    is StoreReadResponse.Error.Message -> RepositoryResponse.Error(origin = response.origin.name, message = response.message)
                }
            }
            else -> RepositoryResponse.Error(origin = "unknown", message = "just error")
        }
    }

}
