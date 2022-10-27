plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val composeVersion = "1.4.0-alpha01"
val composeActivityVersion = "1.7.0-alpha02"
val coreKtxVersion = "1.9.0"
val googlePlayServicesWearableVersion = "18.0.0"
val percentLayoutVersion = "1.0.0"
val wearComposeVersion = "1.1.0-beta01"
val liveDataViewModelVersion = "2.6.0-alpha03"

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
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    name = "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
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
    //implementation("androidx.percentlayout:percentlayout:$percentLayoutVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.wear.compose:compose-material:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-foundation:$wearComposeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0-alpha03")
    implementation("androidx.activity:activity-compose:$composeActivityVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$liveDataViewModelVersion")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}