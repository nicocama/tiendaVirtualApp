plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.googleService)
}

android {
    namespace = "com.example.tiendavirtualapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tiendavirtualapp"
        minSdk = 23
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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.lottie) /*Animaciones*/
    implementation(libs.firebaseAuth) /*Autenticación con FireBase*/
    implementation(libs.firebaseDatabase)/*Base de datos con con FireBase*/
    implementation(libs.imagePicker) /*Recortar una imagen*/
    implementation(libs.glide) /*Leer imagenes*/
    implementation(libs.storage)/*Subir archivos multimedia*/
    implementation(libs.authGoogle)/*Iniciar sesión con Google*/
    implementation(libs.ccp) /*Seleccionar nuestro código telefónico por país*/
    implementation(libs.photoView)/*Zoom imagenes apartado cliente*/
    testImplementation(libs.junit)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}