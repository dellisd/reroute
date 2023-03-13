plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  js(IR) {
    browser()
  }
  jvm()

  sourceSets {
    getByName("commonMain") {
      dependencies {
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.spatialk.geojson)
      }
    }
  }
}
