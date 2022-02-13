package plugin

import ext.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import com.boot.env.Versions

class Junit5Plugin : Plugin<Project> {
    companion object{
        private const val junit5platform = "org.junit:junit-bom:${Versions.junit5}"
        private const val junit5Jupiter = "org.junit.jupiter:junit-jupiter"
    }

    override fun apply(project: Project) {
        project.tasks.withType(Test::class.java) {
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }

        project.dependencies.run {
            testImplementation(platform(junit5platform))
            testImplementation(junit5Jupiter)
        }
    }
}