package scientific.simulation.simba.physics.electromagnetic.models

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D

data class DynamicParticle (
    val definition: ParticleDefinition,
    var kineticEnergy: Double,
    var momentumDirection: Vector3D,
    var properTime : Double
//    val fullEnergy
//        get() = dynamicParticle.kineticEnergy + definition.mass
//    val velocity : Double
//    get() = c_light * sqrt(1 - Math.pow(particle.definition.mass/particle.fullEnergy, 2.0))
)