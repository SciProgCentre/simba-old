package scientific.simba.examples

import hep.dataforge.vision.solid.SolidGroup
import hep.dataforge.vision.solid.box
import hep.dataforge.vision.solid.group
import hep.dataforge.vision.solid.opacity

fun world(worldSize: Double) = SolidGroup {
    opacity = 0.5
    group("Root") {
        box(worldSize, worldSize, worldSize, name = "World")
    }

}