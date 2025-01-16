plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.roomdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.roomdemo"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    api("xyz.doikki.android.dkplayer:dkplayer-java:3.3.7")
    // 可选，包含StandardVideoController的实现
    // 可选，使用exoplayer进行解码
    api("xyz.doikki.android.dkplayer:player-exo:3.3.7")
    // 可选，使用ijkplayer进行解码
    // 可选，如需要缓存或者抖音预加载功能请引入此库
    api("xyz.doikki.android.dkplayer:videocache:3.3.7")
    implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4")

    implementation("io.github.scwang90:refresh-layout-kernel:3.0.0-alpha")     //核心必须依赖
    implementation("io.github.scwang90:refresh-header-classics:3.0.0-alpha")    //经典刷新头
    implementation("io.github.scwang90:refresh-footer-classics:3.0.0-alpha")    //经典加载
    implementation("io.github.scwang90:refresh-header-material:3.0.0-alpha")    //谷歌刷新头
}