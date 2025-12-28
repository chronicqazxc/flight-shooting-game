import java.io.File
import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    // Compose support is enabled through android.buildFeatures.compose
}

fun loadSigningProperties(projectDir: File): Properties {
    val properties = Properties()
    val signingPropertiesFile = File(projectDir, ".secret/signing.properties")
    if (signingPropertiesFile.exists()) {
        signingPropertiesFile.inputStream().use { properties.load(it) }
    } else {
        println("WARNING: .secret/signing.properties not found. Release build will likely fail.")
    }
    return properties
}

repositories {
    google()
    mavenCentral()
}

android {
    namespace = "com.flightgame"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.flightgame"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val signingProperties = loadSigningProperties(rootProject.projectDir)
            storeFile = file(signingProperties.getProperty("storeFile"))
            storePassword = signingProperties.getProperty("storePassword")
            keyAlias = signingProperties.getProperty("keyAlias")
            keyPassword = signingProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
    buildToolsVersion = "36.0.0"
}

    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.material3)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.android)
        testImplementation(libs.junit)
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.turbine)
        androidTestImplementation(libs.androidx.test.junit)
        androidTestImplementation(libs.androidx.test.espresso.core)
        androidTestImplementation(libs.androidx.compose.ui.test.junit4)
        debugImplementation(libs.androidx.compose.ui.tooling)
        debugImplementation(libs.androidx.compose.ui.test.manifest)
    }
