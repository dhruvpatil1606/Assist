plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.text_to_speech"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.text_to_speech"
        minSdk = 28
        targetSdk = 35
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // PDF Reader dependencies
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    
    // CardView for modern UI
    implementation("androidx.cardview:cardview:1.0.0")
    
    // RecyclerView for lists
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // Material Design components
    implementation("com.google.android.material:material:1.11.0")
    
    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}