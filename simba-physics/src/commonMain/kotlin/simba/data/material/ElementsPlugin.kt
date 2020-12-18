package simba.data.material

import hep.dataforge.context.Context
import hep.dataforge.context.PluginFactory
import hep.dataforge.context.PluginTag
import hep.dataforge.meta.Meta
import hep.dataforge.meta.MetaBase
import simba.physics.Element
import simba.data.DataLoader
import kotlin.reflect.KClass


class ElementAnnotation(val Z: Int, val name : String? = null) : MetaBase() {
    //TODO(Load only by name)
    override val items = Meta {
        "Z" put Z
        "name" put name
    }.items

}

interface ElementsLoader : DataLoader<ElementAnnotation, Element> {
    operator fun get(Z: Int, name: String? = null) = load(ElementAnnotation(Z, name))
}

class ElementsPlugin(meta: Meta) : IngredientPlugin<ElementAnnotation, Element>(meta), ElementsLoader {
    override val tag: PluginTag get() = Companion.tag
    val isotopes by require(IsotopesPlugin)
    companion object : PluginFactory<ElementsPlugin> {
        override val tag: PluginTag = PluginTag("element", group = MATERIAL_GROUP)
        override val type: KClass<out ElementsPlugin> = ElementsPlugin::class
        override fun invoke(meta: Meta, context: Context): ElementsPlugin = ElementsPlugin(meta)
    }
}

val Context.elements : ElementsPlugin
    get() = plugins.fetch(ElementsPlugin)
