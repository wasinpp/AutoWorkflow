plugins {
	id("com.android.dynamic-feature")
	id("org.jetbrains.kotlin.android")
	id("plugin.junit")
}
android {
	namespace = "com.boot.dynamicfeature"
	compileSdk = 33
	defaultConfig {
		minSdk = 23
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = JavaVersion.VERSION_11.toString()
	}
}

dependencies {
	implementation(projects.app)
	implementation(libs.appcompat)
	implementation(projects.platform.designSystem)
	implementation(projects.platform.components)
	implementation(platform(libs.compose.bom))
	implementation(libs.bundles.compose)
	implementation(libs.bundles.coroutine)
	implementation(libs.paging.compose)

	androidTestImplementation(platform(libs.compose.bom))
	androidTestImplementation(libs.androidTest.espresso)
	androidTestImplementation(libs.androidTest.junit)
	androidTestImplementation(libs.androidTest.compose)
	debugImplementation(libs.test.composeRule)
}
