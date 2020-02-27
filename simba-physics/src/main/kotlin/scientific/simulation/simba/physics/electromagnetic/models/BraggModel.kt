package scientific.simulation.simba.physics.electromagnetic.models

import org.apache.commons.math3.random.RandomGenerator

class BraggModel(
    override val material: Material,
    override val definition: ParticleDefinition
) : PhysicalModel, IonisationLoss {

    override fun sampleSecondaries(rnd: RandomGenerator, particle: Particle, element: Element): List<Particle> {
        TODO("Implement based on G4BraggModel::SampleSecondaries")
    }

    override fun computeCrossSectionPerAtom(rnd: RandomGenerator, energy: Double, element: Element): Double {
        TODO("Implement based on G4BraggModel::ComputeCrossSectionPerAtom")
    }

    override fun ionizationLoss(rnd: RandomGenerator, kineticEnergy: Double) {
        TODO("Implement based on G4BraggModel::ComputeDEDXPerVolume")
    }
}