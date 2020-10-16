import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion : String by rootProject.extra
val kmathVersion: String by rootProject.extra
val dataforgeVersion: String by rootProject.extra


plugins {
    id("scientifik.jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test-junit"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")

    implementation("org.apache.commons:commons-csv:1.8")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation ("io.github.microutils:kotlin-logging:1.5.4")
    implementation ("org.slf4j:slf4j-simple:1.7.5")

    // << Dataforge >>
    implementation("hep.dataforge:dataforge-context-jvm:$dataforgeVersion")
    implementation("hep.dataforge:dataforge-data-jvm:$dataforgeVersion")
    implementation("hep.dataforge:dataforge-io-jvm:$dataforgeVersion")
    implementation("hep.dataforge:dataforge-meta-jvm:$dataforgeVersion")
    implementation("hep.dataforge:dataforge-tables-jvm:$dataforgeVersion")
    implementation("hep.dataforge:dataforge-workspace-jvm:$dataforgeVersion")
    // <<>>
    
    // << KMath>>
    api("scientifik:kmath-core-jvm:$kmathVersion")
    api("scientifik:kmath-prob-jvm:$kmathVersion")
    // <<>>
    api(project(":simba-core"))
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
//    kotlinOptions.suppressWarnings = true
}