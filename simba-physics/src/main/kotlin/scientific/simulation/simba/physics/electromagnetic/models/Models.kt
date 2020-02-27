package scientific.simulation.simba.physics.electromagnetic.models

import org.apache.commons.math3.random.RandomGenerator

interface PhysicalModel{
    fun sampleSecondaries(rnd: RandomGenerator, particle : Particle, element: Element) : List<Particle>
    fun computeCrossSectionPerAtom(rnd: RandomGenerator, energy :Double, element : Element) : Double
}

interface IonisationLoss{
    val material: Material
    val definition: ParticleDefinition
    fun ionizationLoss(rnd: RandomGenerator, kineticEnergy: Double)
}


//val ChargedGeantion = ParticlesDefinition(
//    "chargedgeantino" , 0.0 * MeV, 0.0 * MeV, +1.0 * eplus,
//    0, 0, 0,
//    0, 0, 0,
//    "geantino", 0, 0, 0,
//    true, -1.0, null,
//    false, "geantino", 0
//);
//
//val Electron = ParticlesDefinition(
//    "e-", electron_mass_c2, 0.0 * MeV, -1.0 * eplus,
//    1, 0, 0,
//    0, 0, 0,
//    "lepton", 1, 0, 11,
//    true, -1.0, null,
//    false, "e", magneticMoment = -muB * 1.00115965218076
//)
//
//val Positron = ParticlesDefinition(
//    "e+", electron_mass_c2, 0.0 * MeV, +1.0 * eplus,
//    1, 0, 0,
//    0, 0, 0,
//    "lepton", -1, 0, -11,
//    true, -1.0, null,
//    false, "e", magneticMoment = muB * 1.00115965218076
//)
//
//val Gamma = ParticlesDefinition(
//    "gamma", 0.0 * MeV, 0.0 * MeV, 0.0,
//    2, -1, -1,
//    0, 0, 0,
//    "gamma", 0, 0, 22,
//    true, -1.0, null,
//    false, "photon", 22
//)