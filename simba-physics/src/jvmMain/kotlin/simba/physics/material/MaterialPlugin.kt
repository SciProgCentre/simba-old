package simba.physics.material

import hep.dataforge.context.Context
import hep.dataforge.context.PluginFactory
import hep.dataforge.context.PluginTag
import hep.dataforge.meta.Meta
import hep.dataforge.meta.MetaBase
import simba.physics.Element
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

interface MaterialLoader : DataLoader<MaterialAnnotation, Material> {
    operator fun get(name: String) = load(MaterialAnnotation(name))
}

class MaterialPlugin(meta: Meta) : IngredientPlugin<MaterialAnnotation, Material>(meta), MaterialLoader {
    override val tag: PluginTag get() = Companion.tag

    companion object : PluginFactory<MaterialPlugin> {
        override val tag: PluginTag = PluginTag("material", group = MATERIAL_GROUP)
        override val type: KClass<out MaterialPlugin> = MaterialPlugin::class
        override fun invoke(meta: Meta, context: Context): MaterialPlugin = MaterialPlugin(meta).apply {
            require(ElementPlugin)
        }
    }
}