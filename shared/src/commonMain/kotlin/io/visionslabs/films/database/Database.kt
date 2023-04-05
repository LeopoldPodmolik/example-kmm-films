package io.visionslabs.films.database

import io.visionslabs.AppDatabase
import io.visionslabs.films.model.Film

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val TAG = "Database"

    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries


    /**
     * Insert on film with mapping to DB entity
     */
    internal fun insertFilm(film: Film): Film? {
        return dbQuery.transactionWithResult {
            dbQuery.insertFilm(
                id = film.id.toLong(),
                adult = (if (film.adult) 1 else 0).toLong(),
                backdropPath = film.backdropPath,
                genreIds = film.genreIds.joinToString(","),
                originalLanguage = film.originalLanguage,
                originalTitle = film.originalTitle,
                overview = film.overview,
                popularity = film.popularity.toDouble(),
                posterPath = film.posterPath,
                releaseDate = film.releaseDate,
                title = film.title,
                video = (if (film.video) 1 else 0).toLong(),
                voteAverage = film.voteAverage.toDouble(),
                voteCount = film.voteCount.toLong(),
                budget = film.budget.toString(),
                revenue = film.revenue.toString(),
            )
            try {
                dbQuery.getOneFilm(id = film.id.toLong(), mapper = ::mapperFilm).executeAsOne()
            } catch (ex: Exception) {
                println("$TAG - insertFilm - error - $ex")
                null
            }
        }
    }

    /**
     * Get one film from DB
     */
    internal fun getFilm(id: Int): Film? {
        return try {
            dbQuery.getOneFilm(id = id.toLong(), mapper = ::mapperFilm).executeAsOne()
        } catch (ex: Exception) {
            println("$TAG - getFilm - error - $ex")
            null
        }
    }

    /**
     * Get all favorites films from DB
     */
    internal fun getAllFavoritesFilms(): List<Film> {
        return dbQuery.getAllFavorites(mapper = ::mapperFilm).executeAsList()
    }

    /**
     * Remove all favorites films in DB
     */
    internal fun removeFilm(id: Int) {
        dbQuery.removeFilm(id = id.toLong())
    }

    /**
     * Map result from DB to kotlin data-class Film
     */
    private fun mapperFilm(
        adult: Long,
        backdropPath: String,
        genreIds: String,
        id: Long,
        originalLanguage: String,
        originalTitle: String,
        overview: String,
        popularity: Double,
        posterPath: String,
        releaseDate: String,
        title: String,
        video: Long,
        voteAverage: Double,
        voteCount: Long,
        budget: String,
        revenue: String
    ): Film {
        return Film(
            id = id.toInt(),
            adult = adult == 1L,
            backdropPath = backdropPath,
            genreIds = when {
                genreIds.isNotEmpty() -> genreIds.split(",").map { it.toInt() }
                else -> listOf()
            },
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity.toFloat(),
            posterPath = posterPath,
            releaseDate = releaseDate,
            title = title,
            video = video == 1L,
            voteAverage = voteAverage.toFloat(),
            voteCount = voteCount.toInt(),
            budget = budget.toLong(),
            revenue = revenue.toLong(),
        )
    }
}