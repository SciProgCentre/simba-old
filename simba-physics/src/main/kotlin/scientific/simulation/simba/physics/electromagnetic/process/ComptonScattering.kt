package ru.mipt.npm.mcengine.physics.process

import ru.mipt.npm.mcengine.CoreSettings
import ru.mipt.npm.mcengine.physics.LongStepPhysicsProcess
import ru.mipt.npm.mcengine.particles.Gamma
import ru.mipt.npm.mcengine.physics.LongStepPhysicalModel
import ru.mipt.npm.mcengine.GEANT4.model.electromagnetic.KleinNishinaCompton
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.EmParameters
import ru.mipt.npm.mcengine.SimulationSettings

class ComptonScattering(override val simulationSettings: SimulationSettings) : LongStepPhysicsProcess() {
    override val physicalModels: Set<LongStepPhysicalModel> = setOf(
            KleinNishinaCompton(
                    simulationSettings.get<EmParameters>().minKinEnergy,
                    simulationSettings.get<CoreSettings>().generator)
    )
    override val name = "ComptonScattering"
    override val appParticles = listOf(Gamma)
    init {
//        SetMinKinEnergyPrim(1*MeV);
//        SetSplineFlag(true);

    }
}