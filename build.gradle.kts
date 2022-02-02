// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Build.androidGradle)
        classpath(Build.kotlinGradlePlugin)
        classpath(Build.GoogleService.classpath)
        classpath(Build.FirebaseAppDistribution.classpath)
        classpath(Build.Spotless.classpath)
        classpath(Build.PlayPublisher.classpath)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.FlowPreview"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
}

plugins {
    id("com.boot.scripts.cd.CDPlugin")
}