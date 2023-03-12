plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.spotless)
}

group = "ca.derekellis.reroute"
version = "1.0-SNAPSHOT"

spotless {
  kotlin {
    target("**/*.kt", "**/*.kts")
    targetExclude("**/build/generated/**/*.*")

    ktlint(libs.versions.ktlint.get()).editorConfigOverride(
      mapOf(
        "indent_size" to "2",
        "ktlint_package-name" to "disabled",
      ),
    )
    trimTrailingWhitespace()
    endWithNewline()
  }
}
