package simba.physics

import kscience.kmath.geometry.Vector3D
import kscience.kmath.geometry.r
import simba.math.*
import kotlin.math.sqrt


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

class ClassicParticle(
    val mass : Double,
    override var kineticEnergy: Double,
    override var momentumDirection: Vector3D,
    position: Vector3D
) : Particle{
    private var position_ : Vector3D = position
    override fun move(shift: Vector3D) {
        position_ += shift
    }

    override val position: Vector3D
        get() = position_

    override val totalMomentum: Double
        get() = sqrt(2*mass*kineticEnergy)
    override val momentum: Vector3D
        get() = totalMomentum*momentumDirection
}

