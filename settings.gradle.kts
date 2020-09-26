rootProject.name = "simba.kt"

pluginManagement {

    val toolsVersion = "0.5.2"

    plugins {
        id("kotlinx.benchmark") version "0.2.0-dev-8"
        id("scientifik.mpp") version toolsVersion
        id("scientifik.jvm") version toolsVersion
        id("scientifik.atomic") version toolsVersion
        id("scientifik.publish") version toolsVersion
        kotlin("plugin.allopen") version "1.4.10"
    }

    repositories {
        mavenLocal()
        jcenter()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/mipt-npm/scientifik")
        maven("https://dl.bintray.com/mipt-npm/dev")
        maven("https://dl.bintray.com/kotlin/kotlinx")
    }
}


include(":simba-core")
include(":simba-physics")
include(":simba-fx-utils")