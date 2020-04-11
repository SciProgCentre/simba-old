package scientific.simulation.simba.physics.data

import hep.dataforge.meta.*
import hep.dataforge.names.Name


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

    var type by string()

    companion object : SchemeSpec<DataSpecification>(::DataSpecification)
}


