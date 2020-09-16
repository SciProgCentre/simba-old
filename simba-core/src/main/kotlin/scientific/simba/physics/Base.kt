package scientific.simba.physics

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import scientific.simba.math.*


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


