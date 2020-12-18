package simba.data

import hep.dataforge.meta.*
import kotlin.js.JsName
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

class ColumnDescription : Scheme() {

    val name by string("")
    val type by string()
    val unit by string()

    @JsName("typeFn")
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
