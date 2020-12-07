package simba.physics.g4.electromagnetic.models.data

import hep.dataforge.context.Context
import hep.dataforge.context.PluginFactory
import hep.dataforge.context.PluginTag
import hep.dataforge.meta.Meta
import hep.dataforge.tables.Table
import hep.dataforge.tables.get
import hep.dataforge.tables.getValue
import hep.dataforge.tables.indices
import simba.physics.data.*
import simba.physics.material.ElementAnnotation
import java.nio.file.Path
import kotlin.reflect.KClass


val ELECTROMAGNETIC_GROUP = "${DataPlugin.ROOT_GROUP}.electromagnetic"

data class ElectromagneticParameters(
    val ionizationPotentials: Double,
    val vFermi: Double, val lFactor: Double
)


interface ElectromagneticParametersLoader : DataLoader<ElementAnnotation, ElectromagneticParameters>{
    operator fun get(Z: Int) = load(ElementAnnotation(Z))
}

class ElectromagneticParametersPlugin(meta: Meta) : DataPlugin<ElementAnnotation, ElectromagneticParameters>(meta), ElectromagneticParametersLoader{

    override val tag: PluginTag = Companion.tag


    companion object: PluginFactory<ElectromagneticParametersPlugin>{
        override val tag = PluginTag("electromagnetic", ELECTROMAGNETIC_GROUP)
        override val type: KClass<out ElectromagneticParametersPlugin> = ElectromagneticParametersPlugin::class

        override fun invoke(meta: Meta, context: Context) = ElectromagneticParametersPlugin(meta)

    }


}

val Context.emParameters: ElectromagneticParametersPlugin
    get() = plugins.fetch(ElectromagneticParametersPlugin)


class G4EMParametersLoader(override val description: Meta, override val table: Table<Any>) : TableLoader<ElementAnnotation, ElectromagneticParameters>(), ElectromagneticParametersLoader{

    override fun findIndx(annotation: ElementAnnotation): Int?{
        val zColumn = table.columns["Z"]
        zColumn?.indices?.forEach {
            if (zColumn.get(it) == annotation.Z) {
                return it
            }
        }
        return null
    }

    override fun getData(indx: Int) : AnnotatedData<ElementAnnotation, ElectromagneticParameters> {
        val Z = table.getValue(indx, "Z", Int::class)!!
        val ionPot = table.getValue(indx, "ionizationPotentials", Double::class)!!
        val fermi = table.getValue(indx, "vFermi", Double::class)!!
        val factor = table.getValue(indx, "lFactor", Double::class)!!
        return AnnotatedData(ElementAnnotation(Z), ElectromagneticParameters(ionPot, fermi, factor))
    }

    companion object: DataLoaderFactory<ElementAnnotation, ElectromagneticParameters> {

        val defaultLocation = dataLocation {
            path = Path.of("GEANT4.zip.df")
            name = "electromagnetic/em_table.df"
        }

        override fun invoke(meta: Meta, context: Context): DataLoader<ElementAnnotation, ElectromagneticParameters> {
            TODO("Not yet implemented")
        }
    }
}