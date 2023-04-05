# Kotlin multiplatform example app for searching films

Example mobile app in kotlin multiplatform *(now implemented only Android part - so iOS is blank)*. 
In shared part is implemented communication with API - [TheMovieDB](https://www.themoviedb.org/), database and repository which is outer point to native part.

## Used libraries
- **Shared - multiplatform**
  - **SqlDeLight** - for save data to database
  - **ktor** - for calling API
  - **store** - for manipulating with data in repository (by repository pattern)
  - **koin** - dependency injection
- **Android**
  - Compose UI
  - Model-View-ViewModel
  - Material design library (v3)
  - Glide for loading images

## Make it works
Because cannot public API KEY from TheMovieDB - it is necessary create own API KEY to work and you can do following:
- create file **Constant** in *shared/src/commonMain/kotlin/io.visionslabs.films* and set :
    object Constant {
      const val APIKEY = "INSERT YOUR API KEY"
    }
- just insert API KEY in MovieDBApi file


## Known limitations
- **iOS emulator** - now cannot run project in iOS emulator because Store library haven't implemented this target, but on physical device it is running. 

## TODO
- Tests
- all iOS part
- Android settings page, better Android UI