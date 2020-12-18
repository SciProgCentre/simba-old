package scientific.simba.examples

import hep.dataforge.context.Global
import hep.dataforge.vision.client.VisionClient
import hep.dataforge.vision.client.fetchAndRenderAllVisions
import hep.dataforge.vision.solid.three.ThreePlugin
import kotlinx.browser.window

fun main() {
    //Loading three-js renderer
    Global.plugins.load(ThreePlugin)
    window.onload = {
        Global.plugins.fetch(VisionClient).fetchAndRenderAllVisions()
    }
}