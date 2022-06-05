import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.kotlin.KtfmtStep.Style
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import com.convention.extensions.InstallSpotlessPreCommitHook
import java.io.File

class SpotlessPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    project.run {
      apply(plugin = "com.diffplug.spotless")
      configure<SpotlessExtension> {
        ratchetFrom = "origin/main"
        kotlin {
          // by default the target is every '.kt' and '.kts` file in the java sourcesets
          ktfmt().style(Style.GOOGLE)
//          ktlint("0.41.0")
//          licenseHeaderFile(rootProject.file("spotless/copyright.kt")) // or licenseHeaderFile
          target("**/*.kt")
          targetExclude("$buildDir/**/*.kt")
          targetExclude("bin/**/*.kt")
        }
      }

      tasks.register<InstallSpotlessPreCommitHook>("installSpotlessHook")
    }

  }
}

private val Project.gitFolder: File
  get() = rootProject.file(".git")

private fun Project.checkGitIsPresent(): Boolean = gitFolder.exists()
