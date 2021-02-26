import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.MAIN_COMPILATION_NAME

plugins {
    id("ru.mipt.npm.gradle.mpp")
    application
}

val ktorVersion : String by rootProject.extra
val kmathVersion: String by rootProject.extra
val dataforgeVersion: String by rootProject.extra

kscience {
    application()
}



kotlin {
    afterEvaluate {
        val jsBrowserDistribution by tasks.getting

        jvm {
            withJava()
            compilations[MAIN_COMPILATION_NAME]?.apply {
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
                implementation("hep.dataforge:visionforge-core:0.2.0-dev-1")
            }
        }
        jvmMain {
            dependencies {
                implementation("hep.dataforge:visionforge-core-jvm:0.2.0-dev-1")
                implementation("org.apache.commons:commons-math3:3.6.1")
                implementation("io.ktor:ktor-server-cio:$ktorVersion")
                implementation("io.ktor:ktor-serialization:$ktorVersion")


                api(project(":simba-core"))
//                api(project(":simba-physics"))

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

            }
        }
        jsMain {
            dependencies {
//                implementation(project(":ui:bootstrap"))
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
            }
        }
    }
}

//application {
//    mainClass.set("scientific.simba.visualisation.MCServerKt")
//}

