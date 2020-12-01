package simba.physics

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import simba.math.*
import simba.math.plus
import simba.math.times


interface Positionable{
    var position : Vector3D
}

fun Positionable.move(shift : Vector3D){
    position += shift
}

interface Dynamicable{
    //FIXME(Interface name)

    var kineticEnergy: Double
    var momentumDirection: Vector3D
    val totalEnergy: Double
    val totalMomentum: Double
    val momentum: Vector3D
    //    val velocity : Double
//    get() = c_light * sqrt(1 - Math.pow(particle.definition.mass/particle.fullEnergy, 2.0))

}

interface Particle : Positionable, Dynamicable

fun Particle.move(shift: Double) = move(shift*momentumDirection)

interface ParticleDefinition{
    val name: String
    val mass: Double
    val width: Double
    val charge: Double
    val iParity: Int
    val iConjugation: Int
    val iIsospin: Int
    val iIsospinZ: Int
    val gParity: Int
    //    val pType: String
    val lepton: Int
    val baryon: Int

    val stable: Boolean
    val lifetime: Double
    //    val decaytable: Any?
    val shortlived: Boolean
//    val subType: String

    val magneticMoment: Double
}

object NeutralFake: ParticleDefinition {
    override val name: String = "NEUTRAL_FAKE"
    override val mass: Double = 0.0
    override val width: Double  = 0.0
    override val charge: Double  = 0.0
    override val iParity: Int  = 0
    override val iConjugation: Int   = 0
    override val iIsospin: Int = 0
    override val iIsospinZ: Int = 0
    override val gParity: Int = 0
    override val lepton: Int = 0
    override val baryon: Int = 0
    override val stable: Boolean = true
    override val lifetime: Double  = 0.0
    override val shortlived: Boolean = false
    override val magneticMoment: Double  = 0.0
}


