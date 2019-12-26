private object Versions {
    const val kotlin = "1.3.61"
    const val spek = "2.0.8"
    const val retrofit = "2.6.2"
    const val motif = "0.3.1"
    const val groupie = "2.7.2"
    const val navComponent = "2.2.0-rc03"
    const val mockk = "1.9.3"
}

object Dep {
    val plugins = Plugins
    val kotlin = Kotlin
    val android = Android

    object Plugins {
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val kotlinSerialization =
            "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:9.1.1"
        const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:0.27.0"
        const val navSafeArgs =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navComponent}"
    }

    object Kotlin {
        private val v = Versions

        const val std = "org.jetbrains.kotlin:kotlin-stdlib:${v.kotlin}"
        const val std8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${v.kotlin}"
        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${v.kotlin}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${v.kotlin}"
        const val kotlinAssertions = "io.kotlintest:kotlintest-assertions:3.4.2"
        const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0"
        const val junit = "junit:junit:4.12"
        const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.15"
        const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:2.4.0"
        const val spekJvm = "org.spekframework.spek2:spek-dsl-jvm:${v.spek}"
        const val spekRuntime = "org.spekframework.spek2:spek-runner-junit5:${v.spek}"
        const val threeTenBp = "org.threeten:threetenbp:1.4.0"
        const val threeTenBpNoTz = "org.threeten:threetenbp:1.4.0:no-tzdb"
        const val okHttpLogging = "com.squareup.okhttp3:logging-interceptor:4.2.1"
        const val retrofit = "com.squareup.retrofit2:retrofit:${v.retrofit}"
        const val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava2:${v.retrofit}"
        const val retrofitKotlinSerialization =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.4.0"
        const val motif = "com.uber.motif:motif:${v.motif}"
        const val motifCompiler = "com.uber.motif:motif-compiler:${v.motif}"
        const val kelm = "com.github.AllanHasegawa:kelm:1.0.3"
        const val mockk = "io.mockk:mockk:${v.mockk}"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:4.2.1"
    }

    object Android {
        private val v = Versions

        const val appCompat = "androidx.appcompat:appcompat:1.1.0"
        const val coreKtx = "androidx.core:core-ktx:1.2.0-rc01"
        const val junit = "androidx.test.ext:junit:1.1.1"
        const val testRunner = "androidx.test:runner:1.1.0"
        const val fragmentTesting = "androidx.fragment:fragment-testing:1.2.0-rc03"
        const val lifecycleExt = "androidx.lifecycle:lifecycle-extensions:2.2.0-rc01"
        const val navComponent = "androidx.navigation:navigation-fragment-ktx:${v.navComponent}"
        const val navComponentUi = "androidx.navigation:navigation-ui-ktx:${v.navComponent}"
        const val materialComponents = "com.google.android.material:material:1.2.0-alpha02"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta3"
        const val picasso = "com.squareup.picasso:picasso:2.71828"
        const val threeTenAbp = "com.jakewharton.threetenabp:threetenabp:1.2.1"
        const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0"
        const val tomo = "com.github.AllanHasegawa:Tomo:0.0.1"
        const val groupie = "com.xwray:groupie:${v.groupie}"
        const val groupieKotlin = "com.xwray:groupie-kotlin-android-extensions:${v.groupie}"
        const val flexbox = "com.google.android:flexbox:2.0.0"
        const val kaspresso = "com.kaspersky.android-components:kaspresso:1.0.1"
        const val mockk = "io.mockk:mockk-android:${v.mockk}"
    }
}
