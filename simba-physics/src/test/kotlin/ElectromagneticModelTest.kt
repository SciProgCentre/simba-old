package ru.mipt.npm.mcengine.geant4

import junit.framework.TestCase
import org.apache.commons.math3.random.JDKRandomGenerator
import ru.mipt.npm.mcengine.CoreSettings
import ru.mipt.npm.mcengine.GEANT4.model.electromagnetic.KleinNishinaCompton
import ru.mipt.npm.mcengine.GEANT4.model.electromagnetic.PEEffectFluoModel
import ru.mipt.npm.mcengine.SimulationSettings
import ru.mipt.npm.mcengine.geant4.material.G4ElementsTable
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.EmParameters
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.model.BetheHeitlerModel
import ru.mipt.npm.mcengine.physics.LongStepPhysicalModel
import ru.mipt.npm.mcengine.utils.CSV_DELIMITER

class ElectromagneticModelTest : TestCase(){
    //TODO(разбить тест на подтесты)
    fun testCrossSection(){

        val generator = JDKRandomGenerator(0)

        val Models = listOf<LongStepPhysicalModel>(
                KleinNishinaCompton(0.0, generator),
                BetheHeitlerModel(generator),
                PEEffectFluoModel(generator)
        )

        val lines = javaClass.getResourceAsStream("/electromagnetic.txt")
                .bufferedReader().readLines().filter { it[0] != '#' && it.isNotEmpty() }
                .map { it.split(CSV_DELIMITER)}
        val headers = lines[0]
        val size = lines.size - 1
        val data = headers.map{ it to Array<Double>(size, {0.0})}.toMap()
        (1 until lines.size).forEach {
            for (pair in (lines[it] zip headers)) {
                data[pair.second]!![it-1] = pair.first.toDouble();
            }
        }

        val element = G4ElementsTable.getElement(1)
        for (model in Models){
            val name = model.javaClass.simpleName
            (data["Energy"]!!.map{model.ComputeCrossSectionPerAtom(it, element)} zip data[name]!!).forEach {
            assertEquals(it.second, it.first, 1e-38) } // Валится на 1e-39 - мой код совпадает с geant4 до 15 знака после запятой
        }





    }

}