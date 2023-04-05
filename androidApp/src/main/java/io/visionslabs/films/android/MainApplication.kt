package io.visionslabs.films.android

import android.app.Application
import io.visionslabs.films.android.main.MainViewModel
import io.visionslabs.films.di.KoinDI
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val androidModule = module {
            viewModel {
                MainViewModel(get())
            }
//            viewModel {
//                SettingViewModel(get())
//            }
        }

        KoinDI().start(appDeclaration = {
            androidContext(androidContext = this@MainApplication)

            modules(androidModule)

        })

    }
}