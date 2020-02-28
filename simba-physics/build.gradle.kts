import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.61"

plugins {
    kotlin("jvm") version  "1.3.61"
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test-junit"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    compile("org.apache.commons:commons-math3:3.6.1")
    compile ("io.github.microutils:kotlin-logging:1.5.4")
    compile ("org.slf4j:slf4j-simple:1.7.5")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
//    kotlinOptions.suppressWarnings = true
}