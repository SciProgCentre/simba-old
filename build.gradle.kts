
val dataforgeVersion by extra("0.1.7")

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/mipt-npm/dataforge")

    }
    group = "scientifik"
    version = "0.0.1"
}



subprojects {
    version = "0.0.1"
}

