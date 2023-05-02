package io.visionslabs.films.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.mockk.every
import io.mockk.mockk
import io.visionslabs.AppDatabase
import io.visionslabs.films.model.Film
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DatabaseTest {

    private lateinit var database: Database

    private val initialFilm = Film(
        adult = false,
        backdropPath = "path",
        genreIds = listOf(1, 2, 3),
        id = 1,
        originalLanguage = "en",
        originalTitle = "example film",
        overview = "This is overview",
        popularity = 10.0f,
        posterPath = "path",
        releaseDate = "2020-01-01",
        title = "Example film",
        video = false,
        voteAverage = 8.5f,
        voteCount = 555,
        budget = 1000000000,
        revenue = 2000000000,
    )

    @Before
    fun before() {
        val databaseDriverFactory = mockk<DatabaseDriverFactory>()
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        every { databaseDriverFactory.createDriver() } returns driver
        AppDatabase.Schema.create(driver)
        database = Database(databaseDriverFactory)
    }


    @Test
    fun insertResult_correct() {
        runBlocking {
            database.insertFilm(film = initialFilm)
            val listFromDB = database.getAllFavoritesFilms()
            Assert.assertEquals(initialFilm.title, listFromDB.first().title)
        }
    }


}