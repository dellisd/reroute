import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import java.util.Properties

buildscript {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
        }
    }
}

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
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            // Add generated KSP code to source set for IDEA autocompletion
            kotlin.srcDir("$buildDir/generated/ksp/js/jsMain/kotlin")

            dependencies {
                implementation(project(":shared"))
                implementation(compose.web.core)
                implementation(compose.runtime)

                implementation(libs.sqldelight.driver.sqljs)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.sqldelight.primitiveAdapters)

//                implementation(libs.spatialk.geojson)
                implementation(libs.inject.runtime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.klock) // TODO: Replace with kotlinx-datetime when formatting is supported
                implementation(libs.compose.routing)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.js)
                implementation(libs.ktor.client.contentNegotiation)
                implementation(libs.ktor.serialization.json)

                implementation(devNpm("copy-webpack-plugin", "9.1.0"))

                implementation(npm("mapbox-gl", libs.versions.mapboxGl.get()))
                implementation(npm("@jlongster/sql.js", libs.versions.sqljs.get()))
                implementation(npm("absurd-sql", libs.versions.absurdSql.get()))
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(libs.sqldelight.runtime)
                implementation(libs.molecule.runtime)
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
        deriveSchemaFromMigrations = true
    }
}

buildkonfig {
    packageName = "ca.derekellis.reroute"
    exposeObjectWithName = "RerouteConfig"

    val props =
        project.rootProject.file("local.properties")
            .takeIf { it.exists() }
            ?.let { Properties().apply { load(it.inputStream()) } }

    defaultConfigs {
        if (props == null) {
            logger.warn("No local.properties file")
            return@defaultConfigs
        }

        if (props.containsKey("mapbox.key")) {
            buildConfigField(STRING, "MAPBOX_ACCESS_KEY", props.getProperty("mapbox.key"))
        } else {
            throw GradleException("mapbox.key not found in local.properties")
        }
    }
}

// TODO: https://github.com/google/ksp/issues/1318
afterEvaluate {
    configurations.filter { it.name.startsWith("generatedByKspKotlinJs") && it.name.endsWith("DependenciesMetadata") }
        .forEach {
            configurations.remove(it)
        }
}
