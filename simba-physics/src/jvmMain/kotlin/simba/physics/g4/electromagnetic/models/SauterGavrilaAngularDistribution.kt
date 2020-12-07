package simba.physics.g4.electromagnetic.models

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.random.RandomGenerator
import simba.math.rotateUz
import simba.physics.HEPParticle
import simba.physics.g4.MeV
import simba.physics.g4.eV
import simba.physics.g4.electron_mass_c2
import simba.physics.g4.twopi
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt


interface EmAngularDistrubition {
    fun sampleDirection(rnd: RandomGenerator, particle: HEPParticle): Vector3D
}

class SauterGavrilaAngularDistribution(
    val emin: Double = 1 * eV, val emax: Double = 100 * MeV
) : EmAngularDistrubition {

    override fun sampleDirection(rnd: RandomGenerator, particle: HEPParticle): Vector3D {
        val energy = max(particle.kineticEnergy, emin)
        return if (energy > emax) {
           particle.momentumDirection
        } else {
            // Initial algorithm according Penelope 2008 manual and
            // F.Sauter Ann. Physik 9, 217(1931); 11, 454(1931).
            // Modified according Penelope 2014 manual
            var costheta = 0.0

            // 1) initialize energy-dependent variables
            // Variable naming according to Eq. (2.24) of Penelope Manual
            // (pag. 44)
            val tau = energy / electron_mass_c2
            val gamma = 1.0 + tau
            val beta = sqrt(tau * (tau + 2.0)) / gamma

            // ac corresponds to "A" of Eq. (2.31)
            //
            val ac = (1.0 - beta) / beta
            val a1 = 0.5 * beta * gamma * tau * (gamma - 2.0)
            val a2 = ac + 2.0
            // gtmax = maximum of the rejection function according to Eq. (2.28),
            // obtained for tsam=0
            val gtmax = 2.0 * (a1 + 1.0 / ac)

            var tsam = 0.0
            var gtr = 0.0

            //2) sampling. Eq. (2.31) of Penelope Manual
            // tsam = 1-cos(theta)
            // gtr = rejection function according to Eq. (2.28)
            do {
                val rand = rnd.nextDouble()
                tsam = 2.0 * ac * (2.0 * rand + a2 * sqrt(rand)) / (a2 * a2 - 4.0 * rand)
                gtr = (2.0 - tsam) * (a1 + 1.0 / (ac + tsam))
            } while (rnd.nextDouble() * gtmax > gtr)

            costheta = 1.0 - tsam

            val sint = sqrt(tsam * (2.0 - tsam))
            val phi = twopi * rnd.nextDouble()

            Vector3D(sint * cos(phi), sint * sin(phi), costheta).rotateUz(particle.momentumDirection)
        }
    }
}