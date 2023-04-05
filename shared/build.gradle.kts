plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.8.0"
    id("app.cash.sqldelight") version "2.0.0-alpha05"
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
//        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    // versions
    val sqlDelightVersion = "2.0.0-alpha05"
    val koinVersion = "3.2.0"
    val ktorVersion = "2.2.3"
    val storeVersion = "5.0.0-alpha04"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                // SQLdelight
                implementation("app.cash.sqldelight:runtime:$sqlDelightVersion")
                implementation("app.cash.sqldelight:coroutines-extensions:$sqlDelightVersion")
                implementation("app.cash.sqldelight:primitive-adapters:$sqlDelightVersion")
                // Koin
                implementation("io.insert-koin:koin-core:$koinVersion")
                // Ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                // Store
                implementation("org.mobilenativefoundation.store:store5:$storeVersion")
                implementation("org.jetbrains.kotlinx:atomicfu:0.18.5")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:android-driver:$sqlDelightVersion")
                implementation("io.insert-koin:koin-android:$koinVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
//        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
//        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
//            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                // SQL de light
                implementation("app.cash.sqldelight:native-driver:$sqlDelightVersion")
                // Ktor
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
//        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
//            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "io.visionslabs.films"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    buildToolsVersion = "33.0.1"
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("io.visionslabs")
        }
    }
}