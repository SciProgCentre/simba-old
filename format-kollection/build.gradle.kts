plugins {
    id("ru.mipt.npm.gradle.mpp")
}


val kotlinx_html_version = "0.7.2"

kotlin {
    sourceSets {
        commonMain {
            dependencies {

            }
        }
        jvmMain{
            dependencies {
                implementation("io.github.microutils:kotlin-logging:1.8.3")
                implementation("org.slf4j:slf4j-simple:1.7.29")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:${kotlinx_html_version}")
            }
        }
    }
}

//tasks.dokkaHtml.configure {
//    outputDirectory.set(buildDir.resolve("dokka"))
//    dokkaSourceSets{
//        configureEach{
//            includes.from("docs.md")
//        }
//    }
//}