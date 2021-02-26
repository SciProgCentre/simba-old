plugins {
    id("ru.mipt.npm.gradle.mpp")
    application
}


group = "scientific.simba.examples"



kscience{
    useSerialization{
        json()
    }
    application()
}

val ktorVersion: String by rootProject.extra

kotlin {
    afterEvaluate {
        val jsBrowserDistribution by tasks.getting

        jvm {
            withJava()
            compilations[org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.MAIN_COMPILATION_NAME]?.apply {
                tasks.getByName<ProcessResources>(processResourcesTaskName) {
                    dependsOn(jsBrowserDistribution)
                    afterEvaluate {
                        from(jsBrowserDistribution)
                    }
                }
            }

        }
    }

    sourceSets {
        commonMain {
            dependencies {

                implementation(project(":simba-core"))
                implementation("hep.dataforge:visionforge-solid:0.2.0-dev-3")
            }
        }
        jvmMain {
            dependencies {
                implementation("hep.dataforge:visionforge-server:0.2.0-dev-3")
            }
        }
        jsMain {
            dependencies {
                implementation("hep.dataforge:visionforge-threejs:0.2.0-dev-3")
            }
        }
    }
}

//application {
//    mainClass.set("scientific.simba.examples.MainKt")
//}
