package scientific.simba.visualisation

import hep.dataforge.vision.solid.SolidManager
import hep.dataforge.meta.Scheme
import hep.dataforge.meta.SchemeSpec
import hep.dataforge.meta.int
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.serialization.json
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI
import java.awt.Desktop
import java.io.File
import java.net.URI


class AppConfig: Scheme(){

    val port by int(8901)

    companion object : SchemeSpec<AppConfig>(::AppConfig)
}



fun Application.module() {
    val currentDir = File(".").absoluteFile
    environment.log.info("Current directory: $currentDir")
    val config  = AppConfig()

    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        json(module = SolidManager.serialModule)
    }
    install(Routing) {
        get("/event") {
//            val event = generator.simulateOne()
//            call.respond(event)
        }
        get("/geometry") {
//            call.respond(Model.buildGeometry())
        }
        static("/") {
            resources()
        }
    }
    try {
        Desktop.getDesktop().browse(URI("http://localhost:${config.port}/index.html"))
    } catch (ex: Exception) {
        log.error("Failed to launch browser", ex)
    }
}

@OptIn(KtorExperimentalAPI::class)
fun main() {
    val config = AppConfig()
    embeddedServer(CIO, config.port, host = "localhost", module = Application::module).start(wait = true)
}