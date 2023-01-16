plugins {
    id("com.convention.composeandroidlib")
    id("plugin.junit")
    id("plugin.spotless")
}

dependencies {
    implementation(libs.bundles.compose)
    implementation(libs.bundles.coroutine)
    implementation(projects.platform.designSystem)
    api(libs.appsflyer)

    androidTestImplementation(libs.androidTest.espresso)
    androidTestImplementation(libs.androidTest.junit)
    androidTestImplementation(libs.androidTest.compose)
    debugImplementation(libs.test.composeRule)
}