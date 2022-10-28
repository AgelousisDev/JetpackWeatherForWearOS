plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("com.github.ben-manes.versions") version "0.43.0"
}

val composeVersion = "1.4.0-alpha01"
val composeActivityVersion = "1.7.0-alpha02"
val coreKtxVersion = "1.9.0"
val googlePlayServicesWearableVersion = "18.0.0"
val percentLayoutVersion = "1.0.0"
val wearComposeVersion = "1.1.0-beta01"
val liveDataViewModelVersion = "2.6.0-alpha03"
val playServicesMapsVersion = "18.1.0"
val playServicesLocationVersion = "21.0.0"
val dataStorePreferenceVersion = "1.0.0"
val navVersion = "2.6.0-alpha03"
val constraintLayoutComposeVersion = "1.1.0-alpha04"

android {
    namespace = "com.agelousis.jetpackweatherwearos"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.agelousis.jetpackweatherwearos"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        /*vectorDrawables {
            useSupportLibrary true
        }*/

    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName(name = "debug")
            isDebuggable = true
            buildConfigField(type = "String", name = "WEATHER_BASE_URL", value = "\"https://api.weatherapi.com/v1/\"")
            buildConfigField(type ="String", name = "WEATHER_API_KEY", value = "\"3299b75bd83b4133b1e52728221706\"")
            buildConfigField(type = "String", name = "WEATHER_API_LOGO_URL", value = "\"https://cdn.weatherapi.com/v4/images/weatherapi_logo.png\"")
            buildConfigField(type = "String", name = "WEATHER_API_WEB_URL", value = "\"https://www.weatherapi.com/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    name = "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
            buildConfigField(type = "String", name = "WEATHER_BASE_URL", value = "\"https://api.weatherapi.com/v1/\"")
            buildConfigField(type ="String", name = "WEATHER_API_KEY", value = "\"3299b75bd83b4133b1e52728221706\"")
            buildConfigField(type = "String", name = "WEATHER_API_LOGO_URL", value = "\"https://cdn.weatherapi.com/v4/images/weatherapi_logo.png\"")
            buildConfigField(type = "String", name = "WEATHER_API_WEB_URL", value = "\"https://www.weatherapi.com/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("com.google.android.gms:play-services-wearable:$googlePlayServicesWearableVersion")
    // Datastore
    implementation("androidx.datastore:datastore-preferences:$dataStorePreferenceVersion")
    //implementation("androidx.percentlayout:percentlayout:$percentLayoutVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.wear.compose:compose-material:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-foundation:$wearComposeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0-alpha03")
    implementation("androidx.activity:activity-compose:$composeActivityVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$liveDataViewModelVersion")
    implementation("androidx.constraintlayout:constraintlayout-compose:$constraintLayoutComposeVersion")
    // Navigation
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-compose:$navVersion")
    // Google Maps
    implementation("com.google.android.gms:play-services-location:$playServicesLocationVersion")
    implementation("com.google.maps.android:maps-compose:2.7.2")
    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.8")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}