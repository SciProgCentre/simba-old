plugins {
    id("scientifik.publish") apply false
    id("org.jetbrains.changelog") version "0.4.0"
}
val kotlinVersion by extra("1.4.10")
val simbaVesrion by extra ("0.0.1")
val dataforgeVersion by extra("0.1.7")
val kmathVersion by extra("0.1.4-dev-1")

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/mipt-npm/dataforge")
        maven("https://dl.bintray.com/mipt-npm/dev")

    }
    group = "scientifik"
    version = simbaVesrion
}



subprojects {
    version = "0.0.1"
}

