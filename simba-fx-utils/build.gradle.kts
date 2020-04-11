import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.61"

plugins {
    kotlin("jvm") version  "1.3.61"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

val dataforgeVersion: String by rootProject.extra

dependencies {

    implementation(project(":simba-physics"))

    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test-junit"))
    compile("no.tornado:tornadofx:1.7.19")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    compile("org.apache.commons:commons-math3:3.6.1")
    compile ("io.github.microutils:kotlin-logging:1.5.4")
    compile ("org.slf4j:slf4j-simple:1.7.5")


    // << Dataforge >>
    implementation("hep.dataforge:dataforge-context-jvm:$dataforgeVersion")
    implementation("hep.dataforge:dataforge-data-jvm:$dataforgeVersion")
    implementation("hep.dataforge:dataforge-io-jvm:$dataforgeVersion")
    implementation("hep.dataforge:dataforge-meta-jvm:$dataforgeVersion")
    implementation("hep.dataforge:dataforge-tables-jvm:$dataforgeVersion")
    implementation("hep.dataforge:dataforge-workspace-jvm:$dataforgeVersion")

    // <<>>
}

javafx {
    modules("javafx.controls")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
//    kotlinOptions.suppressWarnings = true
}