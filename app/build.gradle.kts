plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.ar.sceneform.plugin")

}

android {
    namespace = "com.quran.khalil"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.quran.khalil"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packagingOptions {
        exclude("META-INF/INDEX.LIST")
        exclude("META-INF/DEPENDENCIES")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.nostra13.universalimageloader:universal-image-loader:1.9.5")

    implementation("com.github.f0ris.sweetalert:library:1.5.1")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.android.gms:play-services-identity-license:12.0.1")
    implementation ("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.2.1")


    implementation ("com.google.cloud:google-cloud-speech:2.4.0")

//    implementation ("io.github.sceneview:sceneview:2.0.2")
//    implementation("com.google.ar.sceneform.ux:sceneform-ux:1.17.1")
//    implementation("com.karumi:dexter:5.0.0")
    // Provides ARCore Session and related resources.
    implementation ("com.google.ar:core:1.11.0")

    // Provides ArFragment, and other UX resources.
    implementation ("com.google.ar.sceneform.ux:sceneform-ux:1.11.0")

    // Alternatively, use ArSceneView without the UX dependency.
    implementation ("com.google.ar.sceneform:core:1.11.0")
}
//
sceneform.asset("sampledata/TocoToucan.obj",
    "default",
    "sampledata/TocoToucan.sfa",
    "src/main/assets/TocoToucan")

//apply(plugin = "com.google.ar.sceneform.plugin")