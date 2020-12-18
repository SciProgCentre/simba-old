package simba.physics.g4.electromagnetic.models

import kscience.kmath.geometry.Vector3D
import kscience.kmath.prob.RandomGenerator
import simba.math.rotateUz
import simba.physics.Element
import simba.physics.HEPDiscreteModel
import simba.physics.HEPParticle
import simba.physics.Material
import simba.physics.g4.*
import kotlin.math.*

class BetheHeitlerModel(parametrisation: Parametrisation? = null) : HEPDiscreteModel {
    val parametrisation = parametrisation ?: DEFAULT_PARAMETRISATION

    companion object {
        val DEFAULT_PARAMETRISATION = Parametrisation(
            Parametrisation.Polynom(
                8.7842e+2 * microbarn,
                -1.9625e+3 * microbarn,
                1.2949e+3 * microbarn,
                -2.0028e+2 * microbarn,
                1.2575e+1 * microbarn,
                -2.8333e-1 * microbarn
            ),
            Parametrisation.Polynom(
                -1.0342e+1 * microbarn,
                1.7692e+1 * microbarn,
                -8.2381 * microbarn,
                1.3063 * microbarn,
                -9.0815e-2 * microbarn,
                2.3586e-3 * microbarn
            ),
            Parametrisation.Polynom(
                -4.5263e+2 * microbarn,
                1.1161e+3 * microbarn,
                -8.6749e+2 * microbarn,
                2.1773e+2 * microbarn,
                -2.0467e+1 * microbarn,
                6.5372e-1 * microbarn
            )
        )
    }

    data class Parametrisation(
        val f1: Polynom,
        val f2: Polynom,
        val f3: Polynom
    ) {
        data class Polynom(val a: Double, val b: Double, val c: Double, val d: Double, val e: Double, val f: Double) {
            operator fun invoke(x: Double): Double {
                val x2 = x * x;
                val x3 = x2 * x
                val x4 = x2 * x2
                val x5 = x3 * x2
                return a + b * x + c * x2 + d * x3 + e * x4 + f * x5
            }
        }

        operator fun invoke(Z: Int, X: Double) = (Z + 1.0) * (f1(X) * Z + f2(X) * Z * Z + f3(X))
    }

    fun screenFunction1(screenVariable: Double): Double {
        if (screenVariable > 1.0) {
            return (42.24 - 8.368 * ln(screenVariable + 0.952))
        } else {
            return 42.392 - screenVariable * (7.796 - 1.961 * screenVariable)
        }
    }

    fun screenFunction2(screenVariable: Double): Double {
        if (screenVariable > 1.0) {
            return (42.24 - 8.368 * ln(screenVariable + 0.952))
        } else {
            return (41.405 - screenVariable * (5.828 - 0.8945 * screenVariable))
        }
    }

    override fun sampleSecondaries(rnd: RandomGenerator, particle: HEPParticle, element: Element, material: Material?): List<HEPParticle> {
// The secondaries e+e- energies are sampled using the Bethe - Heitler
// cross sections with Coulomb correction.
// A modified version of the random number techniques of Butcher & Messel
// is used (Nuc Phys 20(1960),15).
//
// GEANT4 internal units.
//
// Note 1 : Effects due to the breakdown of the Born approximation at
//          low energy are ignored.
// Note 2 : The differential cross section implicitly takes account of
//          pair creation in both nuclear and atomic electron fields.
//          However triplet prodution is not generated.

//            const G4Material* aMaterial = couple->GetMaterial();

        val gammaEnergy = particle.kineticEnergy;
        val gammaDirection = particle.momentumDirection;
        val Z = element.Z
        var epsil: Double
        val epsil0 = electron_mass_c2 / gammaEnergy;
        if (epsil0 > 1.0) {
            return emptyList()
        }

        // do it fast if GammaEnergy < Egsmall
        // select randomly one element constituing the material
//            const G4Element* anElement =
//            SelectRandomAtom(aMaterial, theGamma, GammaEnergy);

        val Egsmall = 2.0 * MeV;
        if (gammaEnergy < Egsmall) {

            epsil = epsil0 + (0.5 - epsil0) * rnd.nextDouble();

        } else {
            // now comes the case with GammaEnergy >= 2. MeV

            // Extract Coulomb factor for this Element
            var FZ = 8.0 * ln(Z.toDouble())/3;
            val midEnergy = 50.0 * MeV;
            if (gammaEnergy > midEnergy) {
                FZ += 8.0 * (element.coulomb); }

            // limits of the screening variable
            val screenfac = 136.0 * epsil0 / Z.toDouble().pow(1.0/3.0);
            val screenmax = exp((42.24 - FZ) / 8.368) + 0.952;
            val screenmin = min(4.0 * screenfac, screenmax);

            // limits of the energy sampling
            val epsil1 = 0.5 - 0.5 * sqrt(1.0 - screenmin / screenmax);
            val epsilmin = max(epsil0, epsil1);
            val epsilrange = 0.5 - epsilmin;

            //
            // sample the energy rate of the created electron (or positron)
            //
            //G4double epsil, screenvar, greject ;
            var screenvar: Double
            var greject: Double

            val F10 = screenFunction1(screenmin) - FZ;
            val F20 = screenFunction2(screenmin) - FZ;
            val NormF1 = max(F10 * epsilrange * epsilrange, 0.0);
            val NormF2 = max(1.5 * F20, 0.0);

            do {
                if (NormF1 / (NormF1 + NormF2) > rnd.nextDouble()) {
                    epsil = 0.5 - epsilrange * rnd.nextDouble().pow(1.0 / 3.0)
                    screenvar = screenfac / (epsil * (1 - epsil));
                    greject = (screenFunction1(screenvar) - FZ) / F10;

                } else {
                    epsil = epsilmin + epsilrange * rnd.nextDouble();
                    screenvar = screenfac / (epsil * (1 - epsil));
                    greject = (screenFunction2(screenvar) - FZ) / F20;
                }
            } while (greject < rnd.nextDouble());
        }   //  end of epsil sampling

        //
        // fixe charges randomly
        //

        var ElectTotEnergy: Double
        var PositTotEnergy: Double
        if (rnd.nextDouble() > 0.5) {
            ElectTotEnergy = (1.0 - epsil) * gammaEnergy;
            PositTotEnergy = epsil * gammaEnergy;
        } else {
            PositTotEnergy = (1.0 - epsil) * gammaEnergy;
            ElectTotEnergy = epsil * gammaEnergy;
        }

        //
        // scattered electron (positron) angles. ( Z - axis along the parent photon)
        //
        //  universal distribution suggested by L. Urban
        // (Geant3 manual (1993) Phys211),
        //  derived from Tsai distribution (Rev Mod Phys 49,421(1977))

        val a1 = 1.6;
        val a2 = a1 / 3.0;
        val uu = -ln(rnd.nextDouble() * rnd.nextDouble());
        val u = if (0.25 > rnd.nextDouble()) uu * a1 else uu * a2;

        val thetaEle = u * electron_mass_c2 / ElectTotEnergy;
        val sinte = sin(thetaEle);
        val coste = cos(thetaEle);

        val thetaPos = u * electron_mass_c2 / PositTotEnergy;
        val sintp = sin(thetaPos);
        val costp = cos(thetaPos);

        val phi = twopi * rnd.nextDouble();
        val sinp = sin(phi);
        val cosp = cos(phi);

        //
        // kinematic of the created pair
        //
        // the electron and positron are assumed to have a symetric
        // angular distribution with respect to the Z axis along the parent photon.
        val ElectKineEnergy = max(0.0, ElectTotEnergy - electron_mass_c2)
        val ElectDirection = Vector3D(sinte * cosp, sinte * sinp, coste)
        ElectDirection.rotateUz(gammaDirection)

        // create G4DynamicParticle object for the particle1
        val aParticle1 = HEPParticle(Electron, ElectKineEnergy, ElectDirection, particle.position)

        // the e+ is always created (even with Ekine=0) for further annihilation.

        val PositKineEnergy = max(0.0, PositTotEnergy - electron_mass_c2)
        val PositDirection = Vector3D(-sintp * cosp, -sintp * sinp, costp)
        PositDirection.rotateUz(gammaDirection)

        // create G4DynamicParticle object for the particle2
        val aParticle2 = HEPParticle(Positron, PositKineEnergy, PositDirection, particle.position)

        // kill incident photon
        particle.kineticEnergy = 0.0
//        particle.trackStatus = TrackStatus.stop
        return listOf(aParticle1, aParticle2)
    }


    override fun computeCrossSectionPerAtom(energy: Double, element: Element): Double {
        var xSection = 0.0;
        val Z = element.Z
        if (Z < 0.9 || energy <= 2.0 * electron_mass_c2) {
            return xSection
        }
        val energyLimit = 1.5 * MeV;
        val energy_temp = if (energy < energyLimit) energyLimit else energy
        val energySave = energy;
        var X = ln(energy_temp / electron_mass_c2)
        xSection = parametrisation(Z, X)

        if (energySave < energyLimit) {
            X = (energySave - 2.0 * electron_mass_c2) / (energyLimit - 2.0 * electron_mass_c2);
            xSection *= X * X;
        }

        xSection = max(xSection, 0.0);
        return xSection;
    }
}