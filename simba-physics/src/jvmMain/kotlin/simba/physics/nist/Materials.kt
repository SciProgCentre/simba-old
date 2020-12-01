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
import simba.physics.Element
import simba.physics.Ingredient
import simba.physics.Isotope
import simba.physics.data.*
import simba.physics.material.ElementAnnotation
import simba.physics.material.IsotopeAnnotation
import simba.physics.material.IsotopePlugin
import java.nio.file.Path

data class NISTIsotope(
    override val name: String,
    override val Z: Int,
    override val A: Int,
    override val atomicMass: Double,
    override val m: Int
) : Isotope

class NISTIsotopeLoader(override val description: Meta, val table: Table<Any>) :
    DataLoader<IsotopeAnnotation, NISTIsotope> {

    private fun findIndx(annotation: IsotopeAnnotation): Int? {
        val zColumn = table.columns["Z"]
        val aColumn = table.columns["A"]
        zColumn?.indices?.forEach {
            if (zColumn.get(it) == annotation.Z && aColumn?.get(it) == annotation.A) {
                return it
            }
        }
        return null
    }

    private fun getData(indx: Int): AnnotatedData<IsotopeAnnotation, NISTIsotope> {
        val name = table.getValue(indx, "Symbol").toString()
        val Z = table.getValue(indx, "Z", Int::class)!!
        val A = table.getValue(indx, "A", Int::class)!!
        val atomicMass = table.getValue(indx, "Relative atomic mass", Double::class)!!
        return AnnotatedData(
            IsotopeAnnotation(Z, A),
            NISTIsotope(name, Z, A, atomicMass, 0)
        )
    }

    override fun available(annotation: IsotopeAnnotation) = findIndx(annotation) != null

    override fun load(annotation: IsotopeAnnotation): AnnotatedData<IsotopeAnnotation, NISTIsotope>? {
        val indx = findIndx(annotation) ?: return null
        return getData(indx)
    }

    override fun allItem(): Sequence<AnnotatedData<IsotopeAnnotation, NISTIsotope>> {
        return sequence {
            table.rows.forEach {
                val name = it.getValue("Symbol").toString()
                val Z = it.getValue("Z", Int::class)!!
                val A = it.getValue("A", Int::class)!!
                val atomicMass = it.getValue("Relative atomic mass", Double::class)!!
                yield(
                    AnnotatedData(
                        IsotopeAnnotation(Z, A),
                        NISTIsotope(name, Z, A, atomicMass, 0)
                    )
                )
            }
        }
    }

    companion object : DataLoaderFactory<IsotopeAnnotation, NISTIsotope> {

        val defaultLocation = dataLocation {
            path = Path.of("materials.zip.df")
            name = "isotopes/NIST_isotopes.df"
        }

        override fun invoke(meta: Meta, context: Context): DataLoader<IsotopeAnnotation, NISTIsotope> {
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
data class NISTIsotopeData(
    val A: Int,
    val massFraction: Double
)

@Serializable
data class NISTElementData(
    val name: String,
    val Z: Int,
    val composition: List<NISTIsotopeData>
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
            path = Path.of("materials.zip.df")
            name = "elements/NIST_elements.df"
        }


        override fun invoke(meta: Meta, context: Context): DataLoader<ElementAnnotation, NISTElement> {
            val dataLocation = context.dataLocation("NIST_elements", NISTIsotopeLoader.defaultLocation)
            val envelope = dataLocation.readEnvelope(context)
            val data = envelope?.data?.read {
                Json.decodeFromString<List<NISTElementData>>(readUtf8String())
            }
                ?: error("NIST elements data from ${context.resolveDataPath(dataLocation.path)} not available")
            return NISTElementLoader(envelope.meta)
        }

    }
}