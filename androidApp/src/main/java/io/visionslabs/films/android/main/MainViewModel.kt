package io.visionslabs.films.android.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.visionslabs.films.model.Film
import io.visionslabs.films.respository.Repository
import io.visionslabs.films.respository.RepositoryResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    var searchedText by mutableStateOf("")

    private var _searchResponse = MutableLiveData<RepositoryResponse<List<Film>>>()
    var searchResponse: LiveData<RepositoryResponse<List<Film>>> = _searchResponse

    private var _movieResponse = MutableLiveData<RepositoryResponse<Film>>()
    var movieResponse: LiveData<RepositoryResponse<Film>> = _movieResponse

    private var _isFavoritedFilm = MutableLiveData(false)
    var isFavoritedFilm: LiveData<Boolean> = _isFavoritedFilm

    private var _favoriteFilms = MutableLiveData<List<Film>>(listOf())
    var favoriteFilms: LiveData<List<Film>> = _favoriteFilms

    init {
    }

    fun searchFilms() {
        viewModelScope.launch {
            repository.searchMovieFlow(searchedText).collect() {
                _searchResponse.value = it
            }
        }
    }

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            repository.getMovieDetailFlow(id).collect() {
                _movieResponse.value = it
            }
            _isFavoritedFilm.value = repository.getOneFilm(id) != null
        }
    }

    fun addToFavorites(film: Film) {
        viewModelScope.launch {
            repository.saveToFavorites(film)
            _isFavoritedFilm.value = true
        }
    }

    fun removeFromFavorites(id: Int) {
        viewModelScope.launch {
            repository.removeFromFavorites(id)
            _isFavoritedFilm.value = false
        }
    }

    fun isFavorited(id: Int) {
        viewModelScope.launch {
            _isFavoritedFilm.value = repository.getOneFilm(id) != null
        }
    }

    fun getFavoriteFilms() {
        viewModelScope.launch {
            _favoriteFilms.value = repository.getAllFavoritesFilm()
        }
    }

}