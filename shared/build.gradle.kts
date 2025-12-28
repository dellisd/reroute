plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  js {
    browser()
  }
  jvm()

  sourceSets {
    getByName("commonMain") {
      dependencies {
        api(libs.spatialk.geojson)

        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.serialization.core)
      }
    }
  }
}
