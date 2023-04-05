plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "io.visionslabs.films.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "io.visionslabs.films.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildToolsVersion = "33.0.1"
}

val koinVersion = "3.2.0"
val materialVersion = "1.0.1"
val glideCompVersion = "1.0.0-alpha.1"
val navVersion = "2.5.3"

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.ui:ui-tooling:1.4.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0")
    implementation("androidx.compose.foundation:foundation:1.4.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.4.0")
//    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    // Navigation
    implementation("androidx.navigation:navigation-compose:$navVersion")
    // Flow as state
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    // Material design
    implementation("androidx.compose.material3:material3:$materialVersion")
    implementation("androidx.compose.material3:material3-window-size-class:$materialVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    // Koin
    implementation("io.insert-koin:koin-android:$koinVersion")
    // Glide for compose
    implementation("com.github.bumptech.glide:compose:$glideCompVersion")
}