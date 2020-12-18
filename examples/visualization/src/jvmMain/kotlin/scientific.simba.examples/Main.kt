package scientific.simba.examples


import hep.dataforge.context.Global
import hep.dataforge.names.asName
import hep.dataforge.names.toName
import hep.dataforge.vision.VisionManager
import hep.dataforge.vision.server.close
import hep.dataforge.vision.server.serve
import hep.dataforge.vision.server.show
import hep.dataforge.vision.solid.Solid
import hep.dataforge.vision.solid.SolidManager
import hep.dataforge.vision.solid.color
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.html.h1
import kotlinx.html.script
import kotlin.random.Random

@OptIn(KtorExperimentalAPI::class)
fun main() {
    val world = world(1000.0)

    val context = Global.context("SIMBA") {
        plugin(SolidManager)
    }

    val server = context.plugins.fetch(VisionManager).serve {
        header {
            script {
                src = "visualization.js"
            }
        }
        page {
            h1 { +"Simba demo" }
            vision("main".asName(), world)
        }
        launch {
            delay(1000)
            while (isActive) {
                delay(300)
            }
        }
    }
    server.show()

    println("Press Enter to close server")
    while (readLine()!="exit"){
        //
    }

    server.close()

}