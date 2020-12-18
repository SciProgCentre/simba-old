package simba.physics.material

import hep.dataforge.context.Context
import hep.dataforge.context.PluginFactory
import hep.dataforge.context.PluginTag
import hep.dataforge.meta.Meta
import hep.dataforge.meta.MetaBase
import simba.physics.Material
import simba.physics.data.DataLoader
import simba.physics.data.DataPlugin
import kotlin.reflect.KClass

abstract class IngredientPlugin<M : Meta, T : Any>(meta: Meta) : DataPlugin<M, T>(meta) {
    companion object {
        val MATERIAL_GROUP = "$ROOT_GROUP.material"
    }
}

class MaterialAnnotation(val name : String) : MetaBase() {
    //TODO(Load only by name)
    override val items = Meta {
        "name" put name
    }.items

}

interface MaterialsLoader : DataLoader<MaterialAnnotation, Material> {
    operator fun get(name: String) = load(MaterialAnnotation(name))
}

class MaterialsPlugin(meta: Meta) : IngredientPlugin<MaterialAnnotation, Material>(meta), MaterialsLoader {
    override val tag: PluginTag get() = Companion.tag

    val elements by require(ElementsPlugin)

    companion object : PluginFactory<MaterialsPlugin> {
        override val tag: PluginTag = PluginTag("material", group = MATERIAL_GROUP)
        override val type: KClass<out MaterialsPlugin> = MaterialsPlugin::class
        override fun invoke(meta: Meta, context: Context): MaterialsPlugin = MaterialsPlugin(meta)
    }
}

val Context.materials: MaterialsPlugin
    get() = plugins.fetch(MaterialsPlugin)