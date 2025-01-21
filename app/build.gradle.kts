plugins {
    alias(libs.plugins.android.application)  // Android application plugin
    alias(libs.plugins.kotlin.android)  // Kotlin plugin for Android
    alias(libs.plugins.google.gms.google.services)  // Google services plugin
}

android {
    namespace = "com.example.twostepverification"  // Define the namespace for the app
    compileSdk = 35  // Compile SDK version

    defaultConfig {
        applicationId = "com.example.twostepverification"  // App package ID
        minSdk = 24  // Minimum SDK version
        targetSdk = 35  // Target SDK version
        versionCode = 1  // Version code for the app
        versionName = "1.0"  // Version name for the app

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"  // Test runner
    }

    buildTypes {
        release {
            isMinifyEnabled = false  // Disables Proguard for release builds
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"  // Include proguard rul   es if needed
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11  // Set Java version compatibility
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"  // Set Kotlin JVM target version
    }
}

dependencies {
    // Firebase
    implementation(libs.firebase.auth)  // Firebase Authentication dependency
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-auth")
    // AndroidX libraries
    implementation(libs.androidx.core.ktx)  // Core KTX
    implementation(libs.androidx.appcompat)  // AppCompat
    implementation(libs.material)  // Material Design components
    implementation(libs.androidx.activity)  // Activity components
    implementation(libs.androidx.constraintlayout)  // ConstraintLayout
    implementation(libs.androidx.navigation.fragment.ktx)  // Navigation components

    // Testing dependencies
    testImplementation(libs.junit)  // JUnit for unit tests
    androidTestImplementation(libs.androidx.junit)  // JUnit for Android tests
    androidTestImplementation(libs.androidx.espresso.core)  // Espresso for UI tests
}
