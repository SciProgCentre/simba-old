import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.MAIN_COMPILATION_NAME
import scientifik.jsDistDirectory

val kotlinVersion : String by rootProject.extra
val ktorVersion : String by rootProject.extra
val kmathVersion: String by rootProject.extra
val dataforgeVersion: String by rootProject.extra


plugins {
    id("scientifik.mpp")
    id("application")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    /* Targets configuration omitted. 
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */


    val installJS = tasks.getByName("jsBrowserDistribution")

    js {
        browser {
            dceTask {
                dceOptions {
                    keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
                }
            }
            webpackTask {
                mode = org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode.PRODUCTION
            }
        }
    }

    jvm {
        withJava()
        compilations[MAIN_COMPILATION_NAME]?.apply {
            tasks.getByName<ProcessResources>(processResourcesTaskName) {
                dependsOn(installJS)
                afterEvaluate {
                    from(project.jsDistDirectory)
                }
            }
        }

    }

    sourceSets {
        commonMain  {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
                implementation ("io.github.microutils:kotlin-logging:1.5.4")
                implementation("hep.dataforge:visionforge-solid:0.2.0-dev-1")

            }
        }
        commonTest{
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }


        jvmMain {
            dependencies {
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
                implementation("org.jzy3d:jzy3d-jdt-core:1.0.2")


            }
        }
        jsMain {
            dependencies {

                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
                implementation(npm("text-encoding"))
                implementation(npm("abort-controller"))
                implementation(npm("bufferutil"))
                implementation(npm("utf-8-validate"))
                implementation(npm("fs"))
            }
        }

    }
}

application {
    mainClassName = "scientific.simba.visualisation.MCServerKt"
}

distributions {
    main {
        contents {
            from("$buildDir/libs") {
                rename("${rootProject.name}-jvm", rootProject.name)
                into("lib")
            }
        }
    }
}