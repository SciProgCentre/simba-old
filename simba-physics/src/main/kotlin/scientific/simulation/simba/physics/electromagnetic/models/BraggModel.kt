package scientific.simulation.simba.physics.electromagnetic.models

import mu.KotlinLogging
import org.apache.commons.math3.random.RandomGenerator
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.min

val logger = KotlinLogging.logger {}

class BraggModel(
    override val material: Material,
    override val definition: ParticleDefinition
) : PhysicalModel, IonisationLoss {

    val maxEnergy: Double = 0.0 //FIXME(Подумать откуда должна прийти эта переменная)
    val cutEnergy: Double = 0.0 //FIXME(где оно?)
    val maxKinEnergy: Double = 0.0 //FIXME(где оно?)

    val ratio: Double = electron_mass_c2/definition.mass

    fun MaxSecondaryEnergy (kineticEnergy: Double): Double {
        val tau: Double  = kineticEnergy/definition.mass
        val tmax: Double = 2.0*electron_mass_c2*tau*(tau + 2.0) /
        (1.0 + 2.0*(tau + 1.0)*ratio + ratio*ratio)
        return tmax
    }


    fun ComputeCrossSectionPerElectron(kineticEnergy: Double): Double {

        val cross: Double     = 0.0
        val tmax: Double     = MaxSecondaryEnergy(kineticEnergy)
        val maxEnergy: Double = min(tmax,maxKinEnergy)
        if(cutEnergy < maxEnergy) {

            val energy: Double = kineticEnergy + definition.mass
            val energy_squared: Double = energy * energy
            val beta2: Double = kineticEnergy * (kineticEnergy + 2.0 * definition.mass) / energy_squared
            cross = (maxEnergy - cutEnergy) / (cutEnergy * maxEnergy)
            -beta2 * ln(maxEnergy / cutEnergy) / tmax


            if (0.0 < definition.spin) {
                cross += 0.5 * (maxEnergy - cutEnergy) / energy_squared; } //FIXME(Хьюстон, где спин?)

            cross *= twopi_mc2_rcl2 * definition.charge * definition.charge / beta2
        }
    return cross
    }

    override fun sampleSecondaries(rnd: RandomGenerator, particle: Particle, element: Element): List<Particle> {

        val xmin: Double = 0.0 //FIXME(Что это и откуда берется?)

        val kineticEnergy: Double = particle.dynamicParticle.kineticEnergy
        val tmax: Double = MaxSecondaryEnergy(kineticEnergy)
        val xmax: Double = min(tmax, maxEnergy)
        if(xmin >= xmax) {return emptyList()}


        val energy: Double = kineticEnergy + definition.mass
        val energy_squared: Double = energy*energy
        val beta_squared: Double = kineticEnergy*(kineticEnergy + 2.0*definition.mass)/energy_squared
        val grej: Double  = 1.0
        var deltaKinEnergy: Double = 0.0
        var f: Double = 0.0

        do {
            val rnd1 = rnd.nextDouble()
            val rnd2 = rnd.nextDouble()

            deltaKinEnergy = xmin*xmax/(xmin*(1.0 - rnd1) + xmax*rnd1)

            f = 1.0 - beta_squared*deltaKinEnergy/tmax

            if(f > grej) {
                logger.warn { "Majorant $grej < $f for e = $deltaKinEnergy" }
            }
        } while( grej*rnd2 >= f );


        TODO("Implement based on G4BraggModel::SampleSecondaries")
    }

    override fun computeCrossSectionPerAtom(rnd: RandomGenerator, kineticEnergy: Double, element: Element): Double {
        element.Z * ComputeCrossSectionPerElectron(kineticEnergy)
        TODO("Implement based on G4BraggModel::ComputeCrossSectionPerAtom")
    }

    override fun ionizationLoss(rnd: RandomGenerator, kineticEnergy: Double) {
        TODO("Implement based on G4BraggModel::ComputeDEDXPerVolume")
    }
}