package scientific.simulation.simba.physics.particles

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import scientific.simulation.simba.physics.electromagnetic.models.MeV
import scientific.simulation.simba.physics.electromagnetic.models.electron_mass_c2
import scientific.simulation.simba.physics.electromagnetic.models.eplus
import scientific.simulation.simba.physics.electromagnetic.models.muB

//interface ParticleDefinition{
//    val name: String
//    val mass: Double
//    val width: Double
//    val charge: Double
//    val iParity: Int
//    val iConjugation: Int
//    val iIsospin: Int
//    val iIsospinZ: Int
//    val gParity: Int
////    val pType: String
//    val lepton: Int
//    val baryon: Int
//
//    val stable: Boolean
//    val lifetime: Double
////    val decaytable: Any?
//    val shortlived: Boolean
////    val subType: String
//
//    val magneticMoment: Double
//}

interface PDGID{
    val encoding: Int
    val anti_encoding: Int
}


abstract class AbctractElectron : ParticleDefinition, PDGID
abstract class AbctractPoistron : ParticleDefinition, PDGID
abstract class AbctractGamma : ParticleDefinition, PDGID
abstract class AbctractProton : ParticleDefinition, PDGID


//val Electron = ParticlesDefinition(
//    "e-", electron_mass_c2, 0.0 * MeV, -1.0 * eplus,
//    1, 0, 0,
//    0, 0, 0,
//    "lepton", 1, 0, 11,
//    true, -1.0, null,
//    false, "e", magneticMoment = -muB * 1.00115965218076
//)

object Electron : AbctractElectron() {
    override val name: String = "electron"
        
    override val mass: Double = electron_mass_c2
        
    override val width: Double = 0.0*MeV
        
    override val charge: Double = -1.0* eplus
        
    override val iParity: Int  = 1
        
    override val iConjugation: Int = 0
        
    override val iIsospin: Int = 0
        
    override val iIsospinZ: Int = 0
        
    override val gParity: Int = 0
        
//    override val pType: String
        
    override val lepton: Int = 1
        
    override val baryon: Int = 0
        
    override val encoding: Int = 11
        
    override val stable: Boolean = true
        
    override val lifetime: Double = -1.0
        
//    override val decaytable: Any?
        
    override val shortlived: Boolean = false
        
//    override val subType: String
        
    override val anti_encoding: Int = -11
        
    override val magneticMoment: Double = -muB * 1.00115965218076
        

}

class ParticleGun : PrimaryGenerator {
    var definition = Gamma
    var energy = 1 * MeV
    var momentumDirection = Vector3D(0.0, 0.0, 1.0)
    var number = 1L
    var position = Vector3D(0.0, 0.0, 0.0)
    override fun generatePrimaries(eventContext: EventContext): Sequence<Track> {
        return (1..number).map { Track(Particle(definition, energy, momentumDirection), eventContext.trackCounter.incrementAndGet(), 0, position, 0.0) }.asSequence()

    }
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