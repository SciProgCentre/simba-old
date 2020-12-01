package simba.physics.data

import hep.dataforge.context.AbstractPlugin
import hep.dataforge.context.Factory
import hep.dataforge.meta.Meta

data class AnnotatedData<M : Meta, T : Any>(val annotation: M, val data: T)

interface DataLoader<M : Meta, T : Any> {
    val description: Meta
    fun available(annotation: M): Boolean
    fun load(annotation: M): AnnotatedData<M, T>?
    fun allItem(): Sequence<AnnotatedData<M, T>>
}

interface DataLoaderFactory<M : Meta, T : Any>  : Factory<DataLoader<M,T>>{

}

abstract class DataPlugin<M : Meta, T : Any>(meta: Meta) : AbstractPlugin(meta), DataLoader<M, T> {

    abstract val storage: MutableSet<DataLoader<M, T>>

    fun register(factory: DataLoaderFactory<M, T>) {
        storage.add(factory.invoke(context = context));
    }


    override val description: Meta = meta
    override fun available(annotation: M) = storage.any { it.available(annotation) }

    override fun load(annotation: M): AnnotatedData<M, T>? {
        for  (loader in storage){
            if (loader.available(annotation)){
                return loader.load(annotation)
            }
        }
        return null
    }

    override fun allItem(): Sequence<AnnotatedData<M, T>> {
        return sequence {
            storage.forEach { this.yieldAll(it.allItem()) }
        }
    }

    companion object {
        val ROOT_GROUP = "data"

    }
}






abstract class ListLoader<J : Any, M : Meta, T : Any> : DataLoader<M, T> {
    abstract val data: List<J>

    abstract val selector: (annotation: M, item: J) -> Boolean

    abstract val convertor: (item: J, annotation: M?) -> AnnotatedData<M, T>

    private fun selectItem(annotation: M): J? {
        for (i in 0 until data.size) {
            val item = data[i]
            if (selector(annotation, item)) return item
        }
        return null
    }

    override fun available(annotation: M): Boolean {
        return selectItem(annotation) != null
    }

    override fun load(annotation: M): AnnotatedData<M, T>? {
        val item = selectItem(annotation) ?: return null
        return convertor(item, annotation)
    }

    override fun allItem(): Sequence<AnnotatedData<M, T>> {
        return sequence {
            data.forEach {
                yield(convertor(it, null))
            }
        }
    }

}


