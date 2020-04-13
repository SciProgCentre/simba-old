package scientific.simulation.simba.physics.electromagnetic.models

import org.apache.commons.math3.random.RandomGenerator
import scientific.simulation.simba.physics.particles.ParticleDefinition

interface PhysicalModel{
    fun sampleSecondaries(rnd: RandomGenerator, particle : Particle, element: Element) : List<Particle>
    fun computeCrossSectionPerAtom(rnd: RandomGenerator, energy :Double, element : Element) : Double
}

interface IonisationLoss{
    val material: Material
    val definition: ParticleDefinition
    fun ionizationLoss(rnd: RandomGenerator, kineticEnergy: Double)
}


