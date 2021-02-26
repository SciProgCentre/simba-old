pluginManagement {
    val kotlinVersion = "1.4.30"
    val toolsVersion = "0.8.0"

    repositories {
        gradlePluginPortal()
        jcenter()
        maven("https://repo.kotlin.link")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kotlin/kotlinx")
    }

    plugins {
        id("ru.mipt.npm.gradle.project") version toolsVersion
        id("ru.mipt.npm.gradle.mpp") version toolsVersion
        id("ru.mipt.npm.gradle.jvm") version toolsVersion
        id("ru.mipt.npm.gradle.publish") version toolsVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.allopen") version kotlinVersion
    }
}

rootProject.name = "simba.kt"

include(
    ":simba-core",
    ":simba-physics",
    ":simba-visualisation",
    // examples
    ":examples:visualization",
    ":format-kollection"
)

//include(":simba-fx-utils")



