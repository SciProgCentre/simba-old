package ru.mipt.npm.mcengine.geant4.physics.electromagnetic.model


import org.apache.commons.math3.geometry.euclidean.threed.Vector3D

import org.apache.commons.math3.random.RandomGenerator
import ru.mipt.npm.mcengine.extensions.rotateUz
import ru.mipt.npm.mcengine.particles.Particle
import ru.mipt.npm.mcengine.utils.electron_mass_c2
import ru.mipt.npm.mcengine.utils.eV
import ru.mipt.npm.mcengine.utils.MeV
import ru.mipt.npm.mcengine.utils.twopi


import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

class SauterGavrilaAngularDistribution(val generator: RandomGenerator,
        val emin: Double = 1 * eV, val emax: Double = 100 * MeV) {
    val name = "AngularGenSauterGavrila" //TODO ( G4VEmAngularDistribution)

    fun SampleDirection(particle: Particle) : Vector3D{
        var fLocalDirection : Vector3D
        val energy = max(particle.kineticEnergy , emin)
        if (energy > emax) {
            fLocalDirection = particle.momentumDirection
        } else {
            // Initial algorithm according Penelope 2008 manual and
            // F.Sauter Ann. Physik 9, 217(1931); 11, 454(1931).
            // Modified according Penelope 2014 manual
            var costheta = 0.0

            // 1) initialize energy-dependent variables
            // Variable naming according to Eq. (2.24) of Penelope Manual
            // (pag. 44)
            val tau = energy/ electron_mass_c2
            val gamma = 1.0 + tau
            val beta = sqrt(tau*(tau + 2.0))/gamma

            // ac corresponds to "A" of Eq. (2.31)
            //
            val ac = (1.0 - beta)/beta
            val a1 = 0.5*beta*gamma*tau*(gamma-2.0)
            val a2 = ac + 2.0
            // gtmax = maximum of the rejection function according to Eq. (2.28),
            // obtained for tsam=0
            val gtmax = 2.0*(a1 + 1.0/ac)

            var tsam = 0.0
            var gtr  = 0.0

            //2) sampling. Eq. (2.31) of Penelope Manual
            // tsam = 1-cos(theta)
            // gtr = rejection function according to Eq. (2.28)
            do{
                val rand = generator.nextDouble()
                tsam = 2.0*ac * (2.0*rand + a2*sqrt(rand)) / (a2*a2 - 4.0*rand)
                gtr = (2.0 - tsam) * (a1 + 1.0/(ac+tsam))
                // Loop checking, 03-Aug-2015, Vladimir Ivanchenko
            } while(generator.nextDouble()*gtmax > gtr)

            costheta = 1.0 - tsam

            val sint = sqrt(tsam*(2.0 - tsam))
            val phi  = twopi *generator.nextDouble()

            fLocalDirection = Vector3D(sint*cos(phi), sint*sin(phi), costheta).rotateUz(particle.momentumDirection)
        }
        return fLocalDirection

    }
}