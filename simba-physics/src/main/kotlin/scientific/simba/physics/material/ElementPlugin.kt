package scientific.simba.physics.material

import hep.dataforge.context.Context
import hep.dataforge.context.PluginFactory
import hep.dataforge.context.PluginTag
import hep.dataforge.meta.Meta
import hep.dataforge.meta.MetaBase
import scientific.simba.physics.Element
import scientific.simba.physics.data.DataLoader
import kotlin.reflect.KClass


class ElementAnnotation(val Z: Int, val name : String? = null) : MetaBase() {
    //TODO(Load only by name)
    override val items = Meta {
        "Z" put Z
        "name" put name
    }.items

}

interface ElementLoader : DataLoader<ElementAnnotation, Element> {
    operator fun get(Z: Int, name: String? = null) = load(ElementAnnotation(Z, name))
}

class ElementPlugin(meta: Meta) : IngredientPlugin<ElementAnnotation, Element>(meta), ElementLoader {
    override val tag: PluginTag get() = Companion.tag

    override val storage: MutableSet<DataLoader<ElementAnnotation, Element>> = HashSet()


    companion object : PluginFactory<ElementPlugin> {
        override val tag: PluginTag = PluginTag("element", group = MATERIAL_GROUP)
        override val type: KClass<out ElementPlugin> = ElementPlugin::class
        override fun invoke(meta: Meta, context: Context): ElementPlugin = ElementPlugin(meta).apply {
            require(IsotopePlugin)
        }
    }
}