plugins {
    id("ru.mipt.npm.mpp")
}

val kmathVersion: String by rootProject.extra
val dataforgeVersion: String by rootProject.extra
val explicitApiValue: org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode by rootProject.extra


kscience{
    useCoroutines()
    useSerialization()
}


kotlin {

    explicitApi = explicitApiValue

    sourceSets {
        commonMain {
            dependencies {
                implementation("hep.dataforge:dataforge-context:$dataforgeVersion")
                implementation("hep.dataforge:dataforge-data:$dataforgeVersion")
                implementation("hep.dataforge:dataforge-io:$dataforgeVersion")
                implementation("hep.dataforge:dataforge-meta:$dataforgeVersion")
                implementation("hep.dataforge:dataforge-tables:$dataforgeVersion")
                implementation("hep.dataforge:dataforge-workspace:$dataforgeVersion")

                api("kscience.kmath:kmath-core:$kmathVersion")
                api("kscience.kmath:kmath-prob:$kmathVersion")
                api("kscience.kmath:kmath-geometry:$kmathVersion")
            }
        }
        jvmMain{
            dependencies {

                implementation("org.apache.commons:commons-math3:3.6.1")
                implementation("io.github.microutils:kotlin-logging:1.8.3")
                implementation("org.slf4j:slf4j-simple:1.7.29")
//                api("scientifik:kmath-core-jvm:$kmathVersion")
//                api("scientifik:kmath-prob-jvm:$kmathVersion")
            }
        }
    }
}
