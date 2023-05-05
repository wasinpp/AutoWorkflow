package com.convention

import com.android.build.gradle.LibraryExtension
import com.convention.configs.ProjectBuild
import com.convention.extensions.infra
import com.convention.extensions.kotlinOptions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		with(project.pluginManager) {
			apply("com.android.library")
			apply("kotlin-android")
		}
		project.configure<LibraryExtension> {
			setupSdk(ProjectBuild.create(project.infra))
		}
	}

	private fun LibraryExtension.setupSdk(
		projectBuild: ProjectBuild
	) {
		compileSdk = projectBuild.compileSdk
		defaultConfig {
			minSdk = projectBuild.minSdk

			testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
			consumerProguardFiles("consumer-rules.pro")
		}
		compileOptions {
			sourceCompatibility = projectBuild.java
			targetCompatibility = projectBuild.java
		}
		kotlinOptions {
			jvmTarget = projectBuild.java.toString()
		}
	}
}