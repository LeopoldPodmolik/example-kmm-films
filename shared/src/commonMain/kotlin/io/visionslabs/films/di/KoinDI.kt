package io.visionslabs.films.di

import io.visionslabs.films.database.Database
import io.visionslabs.films.network.MovieDBApi
import io.visionslabs.films.respository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect fun platformModule(): Module

class KoinDI : KoinComponent {

    var appModule = module {
        single { MovieDBApi() }
        single { Database(get()) }
        single { Repository(get(), get()) }
    }

    fun start(appDeclaration: KoinAppDeclaration = {}) = startKoin {
        appDeclaration()
        modules(
            listOf(
                platformModule(),
                appModule
            )
        )
    }
}
