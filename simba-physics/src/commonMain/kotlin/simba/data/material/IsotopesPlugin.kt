package simba.data.material

import hep.dataforge.context.Context
import hep.dataforge.context.PluginFactory
import hep.dataforge.context.PluginTag
import hep.dataforge.meta.Meta
import hep.dataforge.meta.MetaBase
import simba.physics.Isotope
import simba.data.DataLoader
import kotlin.reflect.KClass

class IsotopeAnnotation(val Z: Int, val A: Int, val name: String? = null) : MetaBase() {
    override val items = Meta {
        "Z" put Z
        "A" put A
        "name" put name
    }.items

}

interface IsotopesLoader : DataLoader<IsotopeAnnotation, Isotope> {
    operator fun get(Z: Int, A: Int) = load(IsotopeAnnotation(Z,A))
}

class IsotopesPlugin(meta: Meta) : IngredientPlugin<IsotopeAnnotation, Isotope>(meta), IsotopesLoader {
    override val tag: PluginTag get() = Companion.tag

    companion object : PluginFactory<IsotopesPlugin> {
        override val tag: PluginTag = PluginTag("isotope", group = MATERIAL_GROUP)
        override val type: KClass<out IsotopesPlugin> = IsotopesPlugin::class
        override fun invoke(meta: Meta, context: Context): IsotopesPlugin = IsotopesPlugin(meta)
    }
}

val Context.isotopes : IsotopesPlugin
    get() = plugins.fetch(IsotopesPlugin)