package scientific.simba.physics.data

import hep.dataforge.context.Context
import hep.dataforge.io.Envelope
import hep.dataforge.io.io
import hep.dataforge.io.readEnvelopeFile
import hep.dataforge.meta.*
import hep.dataforge.meta.transformations.MetaConverter
import hep.dataforge.names.Name
import hep.dataforge.values.asValue
import scientific.simba.physics.nist.NISTIsotopeLoader
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass


enum class DescriptionType {
    text,
    markdown,
    html
}

enum class ReferencesType {
    text
}

class DataReferences : Scheme() {
    var type by enum<ReferencesType>(ReferencesType.text)
    var references by stringList()

    companion object : SchemeSpec<DataReferences>(::DataReferences)
}

class DataDescription : Scheme() {
    var type by enum<DescriptionType>(DescriptionType.text)
    var description by string("")

    companion object : SchemeSpec<DataDescription>(::DataDescription)
}


open class DataSpecification : Scheme() {
    var description by spec(DataDescription, DataDescription())

    fun description(block: DataDescription.() -> Unit) {
        description = DataDescription(block)
    }

    var references by spec(DataReferences, DataReferences())

    fun refernces(block: DataReferences.() -> Unit) {
        references = DataReferences(block)
    }

    var links by stringList()

    var type by string("CUSTOM")

    companion object : SchemeSpec<DataSpecification>(::DataSpecification)
}

//fun <T : Configurable> Configurable.spec(
//    spec: Specification<T>, default: T, key: Name? = null
//): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {
//    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
//        val name = key ?: property.name.asName()
//        return config[name].node?.let { spec.wrap(it) } ?: default
//    }
//
//    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
//        val name = key ?: property.name.asName()
//        config[name] = value.config
//    }
//}

class ColumnDescription : Scheme() {

    val name by string("")
    val type by string()
    val unit by string()

    fun type(): KClass<*> = when (type) {
        "Any" -> Any::class
        "Int" -> Int::class
        "Integer" -> Int::class
        "Double" -> Double::class
        else -> Any::class

    }

    companion object : SchemeSpec<ColumnDescription>(::ColumnDescription) {


    }
}

class TableSpecification : DataSpecification() {
    init {
        type = "TABLE"
    }
//    val columns by listValue<ColumnDescription>()


    companion object : SchemeSpec<TableSpecification>(::TableSpecification)
}


val MetaConverter.Companion.path: MetaConverter<Path>
    get() = object : MetaConverter<Path> {
        override fun itemToObject(item: MetaItem<*>): Path = Paths.get(
            when (item) {
                is MetaItem.NodeItem -> item.node[Meta.VALUE_KEY].value ?: error("Can't convert node to a value")
                is MetaItem.ValueItem -> item.value
            }.string
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


fun Context.resolveDataPath(path: Path) = Paths.get(this.properties["dataLocation"].string ?: ".").resolve(path)

fun Context.dataLocation(name: String, default: DataLocation) = this.properties[name]?.spec(DataLocation) ?: this.run {
    this.configure {
        this.update(Meta {
            name put default
        })
    }
    default
}

class DataLocation : Scheme() {

    enum class Type {
        FILE,
        ZIP
    }

    var type by enum(Type.ZIP)
    var path by path(Paths.get("."))
    var name by string("")

    fun readEnvelope(context: Context): Envelope?{
        val path = context.resolveDataPath(this.path)
        return when (this.type) {
            DataLocation.Type.ZIP -> ZipEnvelopeLoader.open(context, path).load(this.name)
            DataLocation.Type.FILE -> context.io.readEnvelopeFile(path)
        }
    }

    companion object : SchemeSpec<DataLocation>(::DataLocation)
}

fun dataLocation(block: DataLocation.() -> Unit) = DataLocation().apply(block)