package io.visionslabs.films.respository

sealed class RepositoryResponse<out T> {
    data class Loading(val origin: String):RepositoryResponse<Nothing>()
    data class Data<T>(val value: T,  val origin: String) : RepositoryResponse<T>()
    data class Error(val message: String,  val origin: String) : RepositoryResponse<Nothing>()
}