import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("ru.mipt.npm.project")
}


val dataforgeVersion by extra("0.2.0")
val kmathVersion by extra("0.1.4-dev-1")
val ktorVersion by extra("1.3.2")
val fxVersion by extra("14")
val explicitApiValue by extra(ExplicitApiMode.Warning)

allprojects {
    repositories {
        mavenLocal()
        maven("https://dl.bintray.com/pdvrieze/maven")
        maven("http://maven.jzy3d.org/releases")
    }
    group = "scientifik"
    version = "0.0.1"


}


subprojects {

}

apiValidation {
    validationDisabled = true
}

