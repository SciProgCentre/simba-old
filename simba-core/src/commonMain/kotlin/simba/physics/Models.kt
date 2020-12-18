package simba.physics

import kscience.kmath.geometry.Vector3D
import kscience.kmath.prob.RandomGenerator

interface AngularDistrubition {
    fun sampleDirection(rnd: RandomGenerator, particle: HEPParticle): Vector3D
}


