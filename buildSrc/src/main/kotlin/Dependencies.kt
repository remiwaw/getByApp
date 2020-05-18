import org.gradle.api.JavaVersion

object Config {
	const val compileSdk = 28
	const val minSdk = 26
	const val targetSdk = 28
    val javaVersion = JavaVersion.VERSION_1_8
    const val buildTools = "29.0.2"
}

object Versions {

	// Build dependencies
	const val detekt = "1.5.1"
	const val ktlint = "9.2.1"
	const val gradle = "3.6.3"
	const val kotlin = "1.3.72"
	const val navigation = "2.1.0"
	const val gradleVersionsPlugin = "0.28.0"

	// Google
	const val room = "2.2.5"
	const val legacySupport = "1.0.0"
	const val activity = "1.1.0-rc03"
	const val appCompat = "1.1.0"
	const val constraintLayout = "1.1.3"
	const val fragment = "1.1.0-alpha09"
	const val ktx = "1.0.2"
	const val lifecycle = "2.2.0-alpha01"
	const val material = "1.1.0-alpha09"
	const val googleServices = "4.3.2"

	// Firebase
	const val firebaseAuthRx = "16.1.0.0"
	const val firebaseCoreRx = "16.0.5.0"
	const val firebaseAuth = "16.0.4"
	const val firebaseCore = "16.0.5"

	// RxJava
	const val rxJava = "2.1.8"
	const val rxKotlin = "2.4.0"
	const val rxAndroid = "2.1.1"

	// 3rd party dependencies
	const val koin = "2.1.5"
	const val gson = "2.8.0"
	const val progressButton = "2.0.0"
	const val carousel = "0.1.5"
	const val rangePicker = "1.3.0"
	const val materialDateRowPicker = "0.7.2"
	const val MPAndroidChart = "v3.0.3"
	const val materialDateTimePicker = "4.2.3"
	const val timber = "4.7.1"
	const val swipeDecorator = "1.2.2"
	const val disposer = "2.0.0"

	// Testing
	const val junit = "4.12"
	const val espresso = "3.1.0"
	const val androidxTesting = "1.1.1"
	const val mockito = "3.1.0"
	const val mockitoKotlin = "2.2.0"
	const val hamcrest = "1.3"
}

object BuildDependencies {
	val toolsDetekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
	val toolsKtlint = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlint}"
	val toolsGradle = "com.android.tools.build:gradle:${Versions.gradle}"
	val toolsKotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
	val toolsGoogleServices = "com.google.gms:google-services:${Versions.googleServices}"
	val navigationSafeArgs =  "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
	val gradleVersionsPlugin =  "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersionsPlugin}"
}

object Dependencies {

	// Google
	const val kotlin ="org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
	const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
	const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
	const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
	const val activityKtx = "androidx.activity:activity-ktx:${Versions.activity}"
	const val lifecycleExt = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
	const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
	const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
	const val material = "com.google.android.material:material:${Versions.material}"
	const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
	const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
	const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

	// 3rd party
	const val progressButton = "com.github.razir.progressbutton:progressbutton:${Versions.progressButton}"
	const val disposer = "io.sellmair:disposer:${Versions.disposer}"
	const val materialDateTimePicker = "com.wdullaer:materialdatetimepicker:${Versions.materialDateTimePicker}"
	const val materialDateRowPicker = "ca.antonious:materialdaypicker:${Versions.materialDateRowPicker}"
	const val MPAndroidChart = "com.github.PhilJay:MPAndroidChart:${Versions.MPAndroidChart}"
	const val rangePicker = "com.savvi.datepicker:rangepicker:${Versions.rangePicker}"
	const val carousel = "com.synnapps:carouselview:${Versions.carousel}"
	const val swipeDecorator = "it.xabaras.android:recyclerview-swipedecorator:${Versions.swipeDecorator}"
	const val gson = "com.google.code.gson:gson:${Versions.gson}"
	const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

	const val firebaseAuthRxKotlin = "com.androidhuman.rxfirebase2:firebase-auth-kotlin:${Versions.firebaseAuthRx}"
	const val firebaseAuthRx = "com.androidhuman.rxfirebase2:firebase-auth:${Versions.firebaseAuthRx}"
	const val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.firebaseAuth}"

	const val firebaseCoreRx = "com.androidhuman.rxfirebase2:firebase-core:${Versions.firebaseCoreRx}"
	const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"

	const val room = "androidx.room:room-runtime:${Versions.room}"
	const val roomKapt = "androidx.room:room-compiler:${Versions.room}"
	const val roomRxJava = "androidx.room:room-rxjava2:${Versions.room}"
	const val testRoom = "androidx.room:room-testing:${Versions.room}"

	const val koin =  "org.koin:koin-core:${Versions.koin}"
	const val koinAndroid =  "org.koin:koin-android:${Versions.koin}"
	const val koinAndroidScope = "org.koin:koin-android-scope:${Versions.koin}"
	const val koinAndroidViewModel = "org.koin:koin-android-viewmodel:${Versions.koin}"

	const val androidxLegacySupport = "androidx.legacy:legacy-support-v4:${Versions.legacySupport}"

	const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
	const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
	const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"

	// Tests
	const val testJunit = "junit:junit:${Versions.junit}"
	const val testMockitoInline = "org.mockito:mockito-inline:${Versions.mockito}"
	const val testMockiktoAndroid = "org.mockito:mockito-android:${Versions.mockito}"
	const val testMockitoKotlin =  "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
	const val testHamcrest = "org.hamcrest:hamcrest-library:${Versions.hamcrest}"

	const val testAndroidxRules = "androidx.test:rules:${Versions.androidxTesting}"
	const val testAndroixRunner = "androidx.test:runner:${Versions.androidxTesting}"
	const val testEspressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"

}

