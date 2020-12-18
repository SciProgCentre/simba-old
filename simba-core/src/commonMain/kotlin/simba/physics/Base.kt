package simba.physics

import kscience.kmath.geometry.Vector3D
import kscience.kmath.geometry.r
import simba.math.*


interface Positionable{
    val position : Vector3D
}

interface MutablePositionable : Positionable{
    fun move(shift : Vector3D)
}


interface Dynamicable{
    //FIXME(Interface name)

    var kineticEnergy: Double
    var momentumDirection: Vector3D
    val momentum: Vector3D
    val totalMomentum: Double
        get() = momentum.r
    //    val velocity : Double
//    get() = c_light * sqrt(1 - Math.pow(particle.definition.mass/particle.fullEnergy, 2.0))

}



interface Particle : MutablePositionable, Dynamicable

fun Particle.move(shift: Double) = move(shift*momentumDirection)




