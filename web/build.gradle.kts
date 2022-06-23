import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildkonfig)
}

group = "ca.derekellis.reroute"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
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
            kotlin.srcDir("$buildDir/generated/ksp/js/jsMain/kotlin")

            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)

                implementation(libs.sqldelight.driver.sqljs)
                implementation(libs.sqldelight.coroutines)

                implementation(libs.spatialk.geojson)
                implementation(libs.inject.runtime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.compose.routing)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.js)
                implementation(libs.ktor.client.contentNegotiation)
                implementation(libs.ktor.client.serialization.json)

                implementation(devNpm("copy-webpack-plugin", "9.1.0"))

                implementation(npm("mapbox-gl", libs.versions.mapboxGl.get()))
                implementation(npm("@jlongster/sql.js", libs.versions.sqljs.get()))
                implementation(npm("absurd-sql", libs.versions.absurdSql.get()))
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(libs.sqldelight.runtime)
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

dependencies {
    add("kspJs", libs.inject.compiler)
}

sqldelight {
    database("RerouteDatabase") {
        packageName = "ca.derekellis.reroute.db"
        generateAsync = true
    }
}

buildkonfig {
    packageName = "ca.derekellis.reroute"
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

// TODO: Remove this once Kotlin/JS upgrades the webpack-cli version
afterEvaluate {
    rootProject.extensions.configure<NodeJsRootExtension> {
        versions.webpackCli.version = "4.10.0"
    }
}
