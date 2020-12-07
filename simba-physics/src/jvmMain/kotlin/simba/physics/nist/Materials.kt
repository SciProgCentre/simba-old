package simba.physics.nist

import hep.dataforge.context.Context
import hep.dataforge.io.readWith
import hep.dataforge.meta.*
import hep.dataforge.tables.Table
import hep.dataforge.tables.get
import hep.dataforge.tables.getValue
import hep.dataforge.tables.indices
import kotlinx.io.text.readUtf8String
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import simba.physics.*
import simba.physics.data.*
import simba.physics.material.*
import java.nio.file.Path


class NISTIsotopeLoader(override val description: Meta, override val table: Table<Any>) :
    TableLoader<IsotopeAnnotation, CommonIsotope>() {

    override fun findIndx(annotation: IsotopeAnnotation): Int? {
        val zColumn = table.columns["Z"]
        val aColumn = table.columns["A"]
        zColumn?.indices?.forEach {
            if (zColumn.get(it) == annotation.Z && aColumn?.get(it) == annotation.A) {
                return it
            }
        }
        return null
    }

    override fun getData(indx: Int): AnnotatedData<IsotopeAnnotation, CommonIsotope> {
        val name = table.getValue(indx, "Symbol").toString()
        val Z = table.getValue(indx, "Z", Int::class)!!
        val A = table.getValue(indx, "A", Int::class)!!
        val atomicMass = table.getValue(indx, "Relative atomic mass", Double::class)!!
        return AnnotatedData(
            IsotopeAnnotation(Z, A),
            CommonIsotope(name, Z, A, atomicMass, 0)
        )
    }

    companion object : DataLoaderFactory<IsotopeAnnotation, CommonIsotope> {

        val defaultLocation = dataLocation {
            path = Path.of("NIST.zip.df")
            name = "materials/isotopes.df"
        }

        override fun invoke(meta: Meta, context: Context): DataLoader<IsotopeAnnotation, CommonIsotope> {
            val dataLocation = context.dataLocation("NIST_isotopes", defaultLocation)
            val envelope = dataLocation.readEnvelope(context)
            val table = envelope?.data?.readWith(CSVTableIOFormat(envelope.meta))
                ?: error("NIST isotopes data from ${context.resolveDataPath(dataLocation.path)} not available")
            return NISTIsotopeLoader(envelope.meta, table)
        }
    }
}

data class NISTElement(
    override val name: String,
    override val Z: Int,
    override val composition: List<Ingredient<Isotope>>
) : Element {

    override val Aeff: Double

    init {
        var wtSum = 0.0
        var aeff = 0.0
        composition.forEach {
            aeff += it.ingredient.atomicMass * it.massFraction
            wtSum += it.massFraction
        }
        assert(wtSum > 0.0)
        if (wtSum > 0.0) {
            aeff /= wtSum
        }
        Aeff = aeff
    }
}


@Serializable
data class NISTElementIsotope(
    val A: Int,
    val massFraction: Double
)

@Serializable
data class NISTElementData(
    val name: String,
    val Z: Int,
    val composition: List<NISTElementIsotope>
)





class NISTElementLoader(
    override val description: Meta,
    override val data: List<NISTElementData>,
    val isotopePlugin: IsotopePlugin
) : ListLoader<NISTElementData, ElementAnnotation, NISTElement>() {

    override val selector: (annotation: ElementAnnotation, item: NISTElementData) -> Boolean
        get() = { annotation: ElementAnnotation, item: NISTElementData ->
            if (annotation.name != null) {
                annotation.Z == item.Z && annotation.name == item.name
            } else {
                annotation.Z == item.Z
            }
        }

    override val convertor: (item: NISTElementData, annotation: ElementAnnotation?) -> AnnotatedData<ElementAnnotation, NISTElement>
        get() = { item: NISTElementData, annotation: ElementAnnotation? ->
            val data = NISTElement(item.name, item.Z, item.composition.map{
                val isotopeAnnotation = IsotopeAnnotation(item.Z, it.A)
                val isotope = isotopePlugin.load(isotopeAnnotation)?.data ?: error("Can't load isotope ${isotopeAnnotation} for element ${annotation}")
                Ingredient(isotope, it.massFraction)
            })
            AnnotatedData(annotation ?: ElementAnnotation(item.Z, item.name), data)
        }

    companion object : DataLoaderFactory<ElementAnnotation, NISTElement> {

        val defaultLocation = dataLocation {
            path = Path.of("NIST.zip.df")
            name = "materials/elements.df"
        }


        override fun invoke(meta: Meta, context: Context): DataLoader<ElementAnnotation, NISTElement> {
            val dataLocation = context.dataLocation("NIST_elements", NISTElementLoader.defaultLocation)
            val envelope = dataLocation.readEnvelope(context)
            val data = envelope?.data?.read {
                Json.decodeFromString<List<NISTElementData>>(readUtf8String())
            }
                ?: error("NIST elements data from ${context.resolveDataPath(dataLocation.path)} not available")
            val isotopePlugin = context.plugins.get(IsotopePlugin) ?: IsotopePlugin(meta, context)
            return NISTElementLoader(envelope.meta, data, isotopePlugin)
        }

    }
}



@Serializable
data class NISTMaterialElement(
    val Z: Int,
    val massFraction: Double? = null,
    val numberOfAtom : Int? = null
)

@Serializable
data class NISTMaterial(
    val name: String,
    val composition: List<NISTMaterialElement>
)


class NISTMaterialLoader(
    override val description: Meta,
    override val data: List<NISTMaterial>,
    val elementPlugin: ElementPlugin
) : ListLoader<NISTMaterial, MaterialAnnotation, CommonMaterial>() {

    override val selector: (annotation: MaterialAnnotation, item: NISTMaterial) -> Boolean
        get() = { annotation: MaterialAnnotation, item: NISTMaterial ->
            annotation.name == item.name
        }

    override val convertor: (item: NISTMaterial, annotation: MaterialAnnotation?) -> AnnotatedData<MaterialAnnotation, CommonMaterial>
        get() = { item: NISTMaterial, annotation: MaterialAnnotation? ->
            val data = CommonMaterial(item.name, item.composition.map{
                val elementAnnotation = ElementAnnotation(it.Z)
                val element = elementPlugin.load(elementAnnotation)?.data ?: error("Can't load isotope ${elementAnnotation} for element ${annotation}")
                Ingredient(element, it.massFraction ?: 1.0) //TODO(NumberOFAtom proccess)
            })
            AnnotatedData(annotation ?: MaterialAnnotation(item.name), data)
        }

    companion object : DataLoaderFactory<MaterialAnnotation, CommonMaterial> {

        val defaultLocation = dataLocation {
            path = Path.of("NIST.zip.df")
            name = "materials/compaund.df"
        }


        override fun invoke(meta: Meta, context: Context): DataLoader<MaterialAnnotation, CommonMaterial> {
            val dataLocation = context.dataLocation("NIST_compaund", NISTMaterialLoader.defaultLocation)
            val envelope = dataLocation.readEnvelope(context)
            val data = envelope?.data?.read {
                Json.decodeFromString<List<NISTMaterial>>(readUtf8String())
            }
                ?: error("NIST materials data from ${context.resolveDataPath(dataLocation.path)} not available")
            val elementPlugin = context.plugins.get(ElementPlugin) ?: ElementPlugin(meta, context)
            return NISTMaterialLoader(envelope.meta, data, elementPlugin)
        }

    }
}