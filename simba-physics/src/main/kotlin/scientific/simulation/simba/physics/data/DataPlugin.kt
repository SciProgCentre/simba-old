package scientific.simulation.simba.physics.data

import java.nio.file.Path
import hep.dataforge.context.*
import hep.dataforge.io.Envelope
import hep.dataforge.io.IOPlugin
import hep.dataforge.io.readEnvelopeFile
import hep.dataforge.meta.*
import hep.dataforge.tables.Table
import hep.dataforge.tables.io.TextRows
import hep.dataforge.tables.io.readEnvelope
import kotlin.reflect.KClass

interface DataLoader<T : Any> {
    fun available(item: MetaItem<*>): Boolean
    fun load(item: MetaItem<*>): T
}

abstract class EnvelopeDataLoader<T: Any>(val io: IOPlugin) : DataLoader<T> {
}

open class TableDataLoader<T: Any>(val path: Path, io: IOPlugin): EnvelopeDataLoader<Table<T>>(io){
    val table by lazy {
        val envelope = io.readEnvelopeFile(path) ?: error("Envelope file $path don't exist or can't be read.")
        val table = TextRows.readEnvelope(envelope)
    }

    override fun available(item: MetaItem<*>): Boolean {
        TODO("Not yet implemented")
    }

    override fun load(item: MetaItem<*>): Table<T> {
        TODO("Not yet implemented")
    }
}

const val SIMBA_GROUP = "simba"



class DataPlugin(meta: Meta) : AbstractPlugin(meta) {
    override val tag: PluginTag get() = IOPlugin.tag

    private val storage: MutableMap<KClass<*>, MutableSet<DataLoader<out Any>>> = HashMap()

    fun <T : Any> register(dataLoader: DataLoader<T>, type: KClass<out T>) {
        if (!storage.containsKey(type)) {
            storage[type] = mutableSetOf()
        }
        storage[type]?.add(dataLoader);
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(item: MetaItem<*>, type: KClass<out T>): T {
        val dataLoader = storage[type]?.firstOrNull {
            it.available(item)
        } ?: error("DataLoader don't exist")
        return dataLoader.load(item) as T
    }

    inline operator fun <reified T : Any> get(item: MetaItem<*>) = get(item, T::class)


    companion object : PluginFactory<DataPlugin> {
        override val tag: PluginTag = PluginTag("data", group = SIMBA_GROUP)
        override val type: KClass<out DataPlugin> = DataPlugin::class
        override fun invoke(meta: Meta, context: Context): DataPlugin = DataPlugin(meta)
    }
}

