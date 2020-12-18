package simba.physics.core

import hep.dataforge.meta.Scheme
import hep.dataforge.meta.string

class SimbaConfiguration: Scheme(){
    var dataLocation by string("")

}


fun simba(block : SimbaConfiguration.() -> Unit) = SimbaConfiguration().apply(block)