import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.shadow)
    application
}

group = "ca.derekellis.reroute"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.autoHead)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.serialization.json)
    implementation(libs.kotlinx.serialization.yaml)
    implementation(libs.logback)
    implementation(libs.inject.runtime)
    implementation(libs.kgtfs.gtfs)
    implementation(libs.clikt)
    implementation(libs.jgrapht)
    implementation(project(":shared"))

    ksp(libs.inject.compiler)
}

kotlin {
    sourceSets {
        getByName("main") {
            kotlin.srcDir("$buildDir/generated/ksp/main/kotlin")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
}

application {
    mainClass.set("ca.derekellis.reroute.server.MainKt")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("${project.name}.jar")
}
