package ru.mipt.npm.mcengine.physics.process


import ru.mipt.npm.mcengine.CoreSettings
import ru.mipt.npm.mcengine.GEANT4.model.electromagnetic.PEEffectFluoModel
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.EmParameters
import ru.mipt.npm.mcengine.particles.Gamma
import ru.mipt.npm.mcengine.physics.LongStepPhysicalModel
import ru.mipt.npm.mcengine.physics.LongStepPhysicsProcess
import ru.mipt.npm.mcengine.SimulationSettings

class PhotoElectricEffect(override val simulationSettings: SimulationSettings) : LongStepPhysicsProcess() {
    override val physicalModels: Set<LongStepPhysicalModel> = setOf(
            PEEffectFluoModel(
                            simulationSettings.get<CoreSettings>().generator,
                            minimalEnergy = simulationSettings.get<EmParameters>().minKinEnergy)
    )
    override val name = "PhotoElectricEffect"
    override val appParticles = listOf(Gamma)
}