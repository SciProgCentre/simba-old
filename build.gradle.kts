plugins {
    id("ru.mipt.npm.gradle.project")
}


val dataforgeVersion by extra("0.3.0")
val kmathVersion by extra("0.2.0")
val ktorVersion by extra("1.3.2")
val fxVersion by extra("14")

allprojects {
    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
        maven("https://dl.bintray.com/pdvrieze/maven")
        maven("http://maven.jzy3d.org/releases")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kotlin/kotlinx")
        maven("https://dl.bintray.com/mipt-npm/dev")
        maven("https://dl.bintray.com/mipt-npm/kscience")
        maven("https://jitpack.io")
    }
    group = "space.kscience"
    version = "0.0.1"
}


subprojects {

}

//apiValidation {
//    validationDisabled = true
//}

