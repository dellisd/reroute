plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.1-rc2"
    id("com.squareup.sqldelight") version "1.5.3"
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
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
    ksp("me.tatarka.inject:kotlin-inject-compiler-ksp:0.4.0")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            // Add generated KSP code to source set for IDEA autocompletion
            kotlin.srcDir("$buildDir/generated/ksp/jsMain/kotlin")

            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation("com.squareup.sqldelight:sqljs-driver:1.5.3")
                implementation("me.tatarka.inject:kotlin-inject-runtime:0.4.0")

                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
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
