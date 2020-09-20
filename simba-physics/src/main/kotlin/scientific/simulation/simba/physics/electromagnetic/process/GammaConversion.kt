package ru.mipt.npm.mcengine.physics.process

import ru.mipt.npm.mcengine.CoreSettings
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.model.BetheHeitlerModel
import ru.mipt.npm.mcengine.particles.Gamma
import ru.mipt.npm.mcengine.physics.LongStepPhysicalModel
import ru.mipt.npm.mcengine.physics.LongStepPhysicsProcess
import ru.mipt.npm.mcengine.SimulationSettings

class GammaConversion(override val simulationSettings: SimulationSettings) : LongStepPhysicsProcess() {
    override val appParticles = listOf(Gamma)
    override val name: String = "GammaConversion"
    override val physicalModels: Set<LongStepPhysicalModel> = setOf(
            BetheHeitlerModel(
                    simulationSettings.get<CoreSettings>().generator
            )
    )
}