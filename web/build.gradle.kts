import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.0"
    id("app.cash.sqldelight") version "2.0.0-alpha01"
    id("com.google.devtools.ksp") version "1.6.10-1.0.4"
    id("com.codingfeline.buildkonfig") version "0.11.0"
}

group = "io.github.dellisd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // TODO: Replace with kspJs once IR is fully supported
    ksp("me.tatarka.inject:kotlin-inject-compiler-ksp:0.4.1")
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            // Add generated KSP code to source set for IDEA autocompletion
            kotlin.srcDir("$buildDir/generated/ksp/jsMain/kotlin")

            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation("app.cash.sqldelight:sqljs-driver:2.0.0-alpha01")
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.0-alpha01")
                implementation("io.github.dellisd.spatialk:geojson:0.1.1")
                implementation("me.tatarka.inject:kotlin-inject-runtime:0.4.1")
                implementation("io.ktor:ktor-client-core:1.6.7")
                implementation("io.ktor:ktor-client-js:1.6.7")
                implementation("io.ktor:ktor-client-serialization:1.6.7")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                implementation("app.softwork:routing-compose:0.1.8")

                implementation(devNpm("copy-webpack-plugin", "9.1.0"))

                implementation(npm("mapbox-gl", "2.7.0"))
                implementation(npm("sql.js", "1.6.2"))
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

sqldelight {
    database("RerouteDatabase") {
        packageName = "io.github.dellisd.reroute.db"
    }
}

buildkonfig {
    packageName = "io.github.dellisd.reroute"
    exposeObjectWithName = "RerouteConfig"

    val props =
        project.rootProject.file("local.properties")
            .takeIf { it.exists() }
            ?.let {
                Properties().apply { load(it.inputStream()) }
            } ?: throw GradleException("local.properties not found")

    defaultConfigs {
        if (props.containsKey("mapbox.key")) {
            buildConfigField(STRING, "MAPBOX_ACCESS_KEY", props.getProperty("mapbox.key"))
        } else {
            throw GradleException("mapbox.key not found in local.properties")
        }
    }
}
