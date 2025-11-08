plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.panicalertsafetyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.panicalertsafetyapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // --- Core Kotlin & AndroidX ---
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    // --- Jetpack Compose (UI) ---
    val composeBom = platform("androidx.compose:compose-bom:2023.08.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // --- Material Components (needed for XML themes like Theme.Material3.DayNight.NoActionBar) ---
    implementation("com.google.android.material:material:1.12.0")
    // Full Material Icons Pack (includes Group, History, Warning, LocationOn, etc.)
    implementation("androidx.compose.material:material-icons-extended")

    // --- Critical Features (Services & Data) ---
    implementation("com.google.android.gms:play-services-location:21.0.1") // Location
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // JSON Serialization
    implementation("com.google.accompanist:accompanist-permissions:0.34.0") // Permissions

    // --- Testing ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
