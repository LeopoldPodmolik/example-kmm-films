package io.visionslabs.films.network

import io.visionslabs.films.model.Film
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSearch(
    @SerialName("page")
    val page: Int,

    @SerialName("results")
    val results: List<Film>,

    @SerialName("total_pages")
    val totalPages: Int,

    @SerialName("total_results")
    val totalResults: Int
)