package simba.data

import hep.dataforge.context.Context
import hep.dataforge.io.Envelope
import hep.dataforge.io.io
import hep.dataforge.io.readEnvelopeFile
import hep.dataforge.meta.*
import hep.dataforge.meta.transformations.MetaConverter
import hep.dataforge.names.Name
import hep.dataforge.names.asName
import hep.dataforge.values.asValue
import hep.dataforge.values.string
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.properties.ReadWriteProperty




val MetaConverter.Companion.path: MetaConverter<Path>
    get() = object : MetaConverter<Path> {
        override fun itemToObject(item: MetaItem<*>): Path = Paths.get(
            when (item) {
                is MetaItem.NodeItem -> item.node[Meta.VALUE_KEY].value ?: error("Can't convert node to a value")
                is MetaItem.ValueItem -> item.value
            }?.string
        )

        override fun objectToMetaItem(obj: Path): MetaItem<*> = MetaItem.ValueItem(obj.toString().asValue())
    }

fun MutableItemProvider.path(key: Name? = null): ReadWriteProperty<Any?, Path?> = item(key).convert(
    MetaConverter.path
)

fun MutableItemProvider.path(default: Path, key: Name? = null): ReadWriteProperty<Any?, Path> = item(key).convert(
    MetaConverter.path
) { default }

fun MutableItemProvider.path(key: Name? = null, default: () -> Path): ReadWriteProperty<Any?, Path> = item(key).convert(
    MetaConverter.path, default
)


fun Context.resolveDataPath(path: Path): Path {
    val properties = this.content(Context.PROPERTY_TARGET)
    val temp = properties.get("dataLocation".asName())
    return Paths.get(
        (temp as? MetaItem.ValueItem).string ?: "."
    )
        .resolve(path)
}

fun Context.resolveDataPath(path: String) = resolveDataPath(Paths.get(path))

fun Context.dataLocation(name: String, default: DataLocation) =
    this.toMeta().get("properties.$name".asName())?.spec(DataLocation) ?: default

class DataLocation : Scheme() {

    enum class Type {
        FILE,
        ZIP
    }

    var type by enum(Type.ZIP)
    var path by path(Paths.get("."))
    var name by string("")

    fun readEnvelope(context: Context): Envelope? {
        val path = context.resolveDataPath(this.path)
        return when (this.type) {
            DataLocation.Type.ZIP -> ZipEnvelopeLoader.open(context, path).load(this.name)
            DataLocation.Type.FILE -> context.io.readEnvelopeFile(path)
        }
    }

    companion object : SchemeSpec<DataLocation>(::DataLocation)
}

fun dataLocation(block: DataLocation.() -> Unit) = DataLocation().apply(block)