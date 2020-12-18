package simba.core

import hep.dataforge.context.AbstractPlugin
import hep.dataforge.context.Context
import hep.dataforge.context.PluginFactory
import hep.dataforge.context.PluginTag
import hep.dataforge.meta.Meta
import kotlin.reflect.KClass

class SimulationPlugin(meta: Meta) : AbstractPlugin(meta) {
    override val tag: PluginTag = Companion.tag


    companion object : PluginFactory<SimulationPlugin>{
        override fun invoke(meta: Meta, context: Context): SimulationPlugin {
            return SimulationPlugin(meta)
        }
        override val tag: PluginTag = PluginTag("simulation", "simba")
        override val type: KClass<out SimulationPlugin> = SimulationPlugin::class

    }
}